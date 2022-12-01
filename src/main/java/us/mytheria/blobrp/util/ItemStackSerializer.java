package us.mytheria.blobrp.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ItemStackSerializer {

    @Nullable
    public static String toBase64(ItemStack itemStack) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(itemStack);
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static ItemStack fromBase64(String base64) {
        if (base64 == null) {
            return null;
        }
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteArrayInputStream)) {
            ItemStack itemStack = (ItemStack) objectInputStream.readObject();
            return itemStack;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean serialize(ItemStack itemStack, YamlConfiguration configuration) {
        return serialize(itemStack, configuration, "ItemStack");
    }

    public static boolean serialize(ItemStack itemStack, YamlConfiguration configuration, String path) {
        String base64 = toBase64(itemStack);
        if (base64 == null)
            return false;
        configuration.set(path, base64);
        return true;
    }

    @Nullable
    public static ItemStack deserialize(YamlConfiguration configuration, String path) {
        return fromBase64(configuration.getString(path));
    }

    @Nullable
    public static ItemStack deserialize(YamlConfiguration configuration) {
        return deserialize(configuration, "ItemStack");
    }

}
