package us.mytheria.blobrp.util;

import us.mytheria.bloblib.entities.message.BlobActionBar;
import us.mytheria.bloblib.entities.message.BlobMessage;
import us.mytheria.bloblib.entities.message.BlobTitle;
import us.mytheria.blobrp.BlobRP;

import java.util.ArrayList;
import java.util.List;

public class BlobMessageLib {

    public static BlobTitle defaultTitle(String title, String subtitle) {
        return new BlobTitle(title, subtitle, 0, 12, 0);
    }

    public static BlobActionBar defaultLangManagerActionBar(String key) {
        return new BlobActionBar(BlobRP.getInstance().getDirector().getLangManager().getLang(key));
    }

    public static BlobTitle defaultLangManager(String key) {
        String[] title = BlobRP.getInstance().getDirector().getLangManager().getTitle(key);
        return defaultTitle(title[0], title[1]);
    }

    public static BlobTitle defaultSubtitle(String key, String regex, String replacement) {
        String[] title = BlobRP.getInstance().getDirector().getLangManager().getTitle(key);
        return defaultTitle(title[0], title[1].replaceAll(regex, replacement));
    }

    public static List<BlobMessage> detailedDefault(String titleKey, String actionbarKey) {
        List<BlobMessage> messages = new ArrayList<>();
        messages.add(defaultLangManager(titleKey));
        messages.add(defaultLangManagerActionBar(actionbarKey));
        return messages;
    }
}
