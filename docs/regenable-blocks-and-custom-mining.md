# Regenable Blocks & BlobDesign Custom Mining

## Regenable Blocks

Regenable blocks are blocks that automatically respawn after being broken. When a player breaks a regenable block, it temporarily turns into a different "broken" variant, then restores to the original after a configurable delay.

Regenable blocks work for **both vanilla Minecraft blocks and BlobDesign preset blocks**, without requiring the custom mining system to be enabled.

### Configuration Files

Regenable blocks are configured via individual YAML files placed in:

```
plugins/BlobRP/RegenableBlockData/
```

Each file defines one regenable block type. The filename (without `.yml`) becomes the internal key and must be unique.

### YAML Format — Vanilla Blocks

```yaml
Material: IRON_ORE

NewBlockType:
  Material: COBBLESTONE

Delay:
  Start: 200
  End: 400

Priority: 1
```

| Field | Description |
|---|---|
| `Material` | The original block material. Use the Bukkit `Material` enum name (e.g. `IRON_ORE`, `STONE`, `DIAMOND_ORE`). |
| `NewBlockType.Material` | The block to place immediately after break. Must be a **vanilla** material (e.g. `COBBLESTONE`, `BEDROCK`). BlobDesign types are not allowed here. |
| `Delay.Start` | Minimum ticks before regeneration (20 ticks = 1 second). Must be greater than 0. |
| `Delay.End` | Maximum ticks before regeneration. Must be greater than 0. A random value between `Start` and `End` inclusive is chosen each time. |
| `Priority` | Integer. Higher priority wins when multiple regenable configs could match the same block. Default is `1`. `AIR` and `BEDROCK` always have priority `0`. |

### YAML Format — BlobDesign Blocks

```yaml
BlockType:
  DisplayElementType: BLOCK_DISPLAY
  Key: my_custom_ore

NewBlockType:
  Material: COBBLESTONE

Delay:
  Start: 300
  End: 600

Priority: 2
```

| Field | Description |
|---|---|
| `BlockType.DisplayElementType` | The display element type of the preset block: `BLOCK_DISPLAY` or `ITEM_DISPLAY`. |
| `BlockType.Key` | The preset key from the BlobDesign configuration. |
| `NewBlockType.Material` | Same as vanilla — the temporary block after break. Must be vanilla. |
| `Delay.Start` / `Delay.End` | Same as vanilla. |
| `Priority` | Same as vanilla. |

> **Important:** `NewBlockType` cannot be a BlobDesign type. It must always be a vanilla block material. Attempting to use a BlobDesign type will throw a runtime error when the regenable triggers.

### Requirements

- **Vanilla blocks:** No extra plugins required. The `RegenableBlockDirector` is always active.
- **BlobDesign blocks:** Requires the `BlobDesign` plugin to be installed and enabled.

### How It Works

1. A player breaks a block that matches a regenable config. The match is detected via the `BlockBreakEvent` for vanilla blocks, or via `CustomBlockBreakEvent` for BlobDesign blocks.
2. The block immediately turns into `NewBlockType` (the "broken" state).
3. A scheduled task runs after a random delay between `Start` and `End` ticks.
4. When the task runs, it checks if the block is still "allowed" — i.e. no higher-priority regenable has modified the block in the meantime. If allowed, it restores the block to the original type. If not (e.g. the block was broken again by another regenable), it cleans itself up without restoring.
5. On `/blobrp reload` or plugin shutdown, all pending regenerations are force-completed immediately.

### Priority System

When a regenable block is about to regen, it checks whether its own priority is **greater than or equal to** the current block type's priority:

- The current block's priority is determined by looking up which regenable config matches its current type. If no config matches, the priority defaults to `1`.
- `AIR` and `BEDROCK` are hardcoded to priority `0`.
- If two regenable configs reference the same block type, the duplicate is rejected at load time — only the first loaded config wins.

This is relevant for **chains of regenable blocks** where one regenable's `NewBlockType` overlaps with another regenable's `BlockType`.

### Example Scenarios

**Diamond ore regenerates after 60–120 seconds:**

```yaml
# File: plugins/BlobRP/RegenableBlockData/diamond_ore.yml
Material: DIAMOND_ORE

NewBlockType:
  Material: BEDROCK

Delay:
  Start: 1200
  End: 2400

Priority: 1
```

**Cracked stone bricks repair themselves after 10–30 seconds:**

```yaml
# File: plugins/BlobRP/RegenableBlockData/cracked_bricks.yml
Material: CRACKED_STONE_BRICKS

NewBlockType:
  Material: STONE_BRICKS

Delay:
  Start: 200
  End: 600

Priority: 1
```

**BlobDesign custom ore with a 30–60 second respawn:**

```yaml
# File: plugins/BlobRP/RegenableBlockData/custom_ore.yml
BlockType:
  DisplayElementType: BLOCK_DISPLAY
  Key: custom_diamond_ore

NewBlockType:
  Material: COBBLESTONE

Delay:
  Start: 600
  End: 1200

Priority: 1
```

---

## BlobDesign Custom Mining

Custom mining gives **BlobDesign preset blocks** (display-entity-based blocks) vanilla-like breaking mechanics, including configurable hardness, required tools, preferred tools, and progressive break animations.

### Prerequisites

- **BlobDesign** plugin must be installed and enabled.
- **Minecraft 1.20.5 or higher** (uses the `Breaking` API introduced in that version).

### Configuration

Custom mining is configured in the main plugin configuration file:

```
plugins/BlobRP/config.yml
```

Under `Listeners.ComplexListeners.BlobDesign-Custom-Mining`:

```yaml
Listeners:
  ComplexListeners:
    BlobDesign-Custom-Mining:
      Register: true
      Values:
        BLOCK_DISPLAY:
          my_custom_stone:
            Hardness: 3.0
            Requires-Tool-TagSet: BlobRP.Pickaxes
            Preferred-Tool-TagSet: BlobRP.Pickaxes
          my_custom_wood:
            Hardness: 2.0
            Preferred-Tool-TagSet: BlobRP.Axes
        ITEM_DISPLAY:
          my_item_block:
            Hardness: 1.5
```

| Field | Description |
|---|---|
| `Register` | Set to `true` to enable custom mining. |
| `BLOCK_DISPLAY` | Map of BlobDesign `BLOCK_DISPLAY` preset keys to their `CustomBlock` definitions. |
| `ITEM_DISPLAY` | Map of BlobDesign `ITEM_DISPLAY` preset keys to their `CustomBlock` definitions. |

Each entry under `BLOCK_DISPLAY` or `ITEM_DISPLAY` accepts:

| Field | Required | Description |
|---|---|---|
| `Hardness` | Yes | Float value. Break time formula: `breakSpeed / Hardness`. Values above `30` make the block effectively instant-break with the correct tool. |
| `Requires-Tool-TagSet` | No | A TagSet key. If the held item does not match this TagSet, break speed is divided by `100` instead of `30`, making it extremely slow. If omitted, any item can break the block. |
| `Preferred-Tool-TagSet` | No | A TagSet key. Intended for tools that mine faster. Currently informational / available via the API — the break speed multiplier is based on `Requires-Tool-TagSet`, not this field. |

### How It Works

1. When a player starts damaging a BlobDesign preset block (`BlockDamageEvent`), the listener checks if the block's preset key is configured.
2. The break speed is calculated: `breaking.getBreakSpeed(player) / Hardness`.
3. If the player is holding a tool matching `Requires-Tool-TagSet`, the damage is divided by `30`. Otherwise, it is divided by `100`.
4. **Instant breaking:** If `damage > 1`, the block breaks immediately, a `CustomBlockBreakEvent` is fired, and the preset block is despawned.
5. **Progressive breaking:** If damage is less than 1, a `Breaker` task runs. It calculates `ticks = ceil(1 / damage)` and progressively shrinks the display entity's scale over those ticks, simulating the vanilla cracking animation.
6. If the player stops damaging (`BlockDamageAbortEvent`), the display entity's scale is reset and the break task is cancelled.
7. When the block fully breaks, `CustomBlockBreakEvent` is fired. Other listeners (PhatLoots, regenable) may react to this event.

### TagSets for Tools

TagSets are globally defined collections of item identifiers. You can reference any existing TagSet by its key. For example:

- `BlobRP.Pickaxes` — could contain `WOODEN_PICKAXE`, `STONE_PICKAXE`, `IRON_PICKAXE`, etc.
- `BlobRP.Axes` — could contain `WOODEN_AXE`, `STONE_AXE`, etc.

If a player holds an item matching the `Requires-Tool-TagSet`, mining is 30x faster than without the correct tool.

### Default Configuration

By default, custom mining is disabled:

```yaml
BlobDesign-Custom-Mining:
  Register: false
  Values:
    BLOCK_DISPLAY: { }
    ITEM_DISPLAY: { }
```

### Example Configurations

**Custom stone block that requires a pickaxe:**

```yaml
BLOCK_DISPLAY:
  custom_stone:
    Hardness: 3.0
    Requires-Tool-TagSet: BlobRP.Pickaxes
    Preferred-Tool-TagSet: BlobRP.Pickaxes
```

**Custom wood block that mines faster with an axe:**

```yaml
BLOCK_DISPLAY:
  custom_oak_log:
    Hardness: 2.0
    Preferred-Tool-TagSet: BlobRP.Axes
```

**Breakable by any tool (no requirements):**

```yaml
BLOCK_DISPLAY:
  custom_soil:
    Hardness: 0.8
```

---

## Using Both Together

Regenable blocks and custom mining work together for BlobDesign preset blocks. The flow is:

1. Player breaks a BlobDesign preset block (custom mining controls speed / tool requirements).
2. A `CustomBlockBreakEvent` is fired.
3. The regenable listener (`BlobDesignCustomMining.onRegenable`) checks if the broken block's type matches a regenable config.
4. If it does, the block is immediately replaced with `NewBlockType` and scheduled for regeneration.

### Full Example: Custom Ore with Hardness, Loot, and Regen

**1. Regenable block config** (`plugins/BlobRP/RegenableBlockData/custom_ore.yml`):

```yaml
BlockType:
  DisplayElementType: BLOCK_DISPLAY
  Key: custom_diamond_ore

NewBlockType:
  Material: BEDROCK

Delay:
  Start: 1200
  End: 2400

Priority: 1
```

**2. Custom mining config** (in `plugins/BlobRP/config.yml`):

```yaml
Listeners:
  ComplexListeners:
    BlobDesign-Custom-Mining:
      Register: true
      Values:
        BLOCK_DISPLAY:
          custom_diamond_ore:
            Hardness: 3.0
            Requires-Tool-TagSet: BlobRP.Pickaxes
            Preferred-Tool-TagSet: BlobRP.Pickaxes
```

**Result:** The custom diamond ore can only be mined effectively with a pickaxe, takes time proportional to hardness 3.0, drops loot (if a PhatLoot is linked), and regenerates after 60–120 seconds.

### Can I use regenable blocks without custom mining?

Yes. Regenable blocks for **vanilla blocks** work out of the box — no custom mining setup needed. Custom mining is only required if you want:
- Custom break speeds / hardness for BlobDesign preset blocks
- Tool requirements for BlobDesign preset blocks
- Progressive break animations for BlobDesign preset blocks
