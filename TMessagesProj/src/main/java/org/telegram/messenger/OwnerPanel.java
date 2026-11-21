package org.telegram.messenger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OwnerPanel {
    private static SharedPreferences getPrefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("owner_panel_prefs", Context.MODE_PRIVATE);
    }

    public static String getCustomPhone(String originalPhone) {
        String custom = getPrefs().getString("custom_phone", "");
        return custom.isEmpty() ? originalPhone : custom;
    }

    public static String getCustomUsername(String originalUsername) {
        String custom = getPrefs().getString("custom_username", "");
        return custom.isEmpty() ? originalUsername : custom;
    }

    public static long getCustomStars(long originalStars) {
        long custom = getPrefs().getLong("custom_stars", -1);
        return custom >= 0 ? custom : originalStars;
    }

    public static void showAdminDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("👑 Панель Владельца");

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        final EditText phoneInput = new EditText(context);
        phoneInput.setHint("Номер (например: +888 0000 0000)");
        phoneInput.setText(getPrefs().getString("custom_phone", ""));
        layout.addView(phoneInput);

        final EditText userInput = new EditText(context);
        userInput.setHint("Юзернейм без @ (например: durov)");
        userInput.setText(getPrefs().getString("custom_username", ""));
        layout.addView(userInput);

        final EditText starsInput = new EditText(context);
        starsInput.setHint("Количество Stars (например: 1000000)");
        long savedStars = getPrefs().getLong("custom_stars", -1);
        if (savedStars >= 0) starsInput.setText(String.valueOf(savedStars));
        layout.addView(starsInput);

        builder.setView(layout);

        builder.setPositiveButton("Сохранить и выдать", (dialog, which) -> {
            SharedPreferences.Editor editor = getPrefs().edit();
            editor.putString("custom_phone", phoneInput.getText().toString().trim());
            editor.putString("custom_username", userInput.getText().toString().trim());
            String starsStr = starsInput.getText().toString().trim();
            if (!starsStr.isEmpty()) {
                try {
                    editor.putLong("custom_stars", Long.parseLong(starsStr));
                } catch (Exception ignored) {}
            } else {
                editor.putLong("custom_stars", -1);
            }
            editor.apply();
        });

        builder.setNegativeButton("Сбросить всё", (dialog, which) -> {
            getPrefs().edit().clear().apply();
        });

        builder.show();
    }
}
