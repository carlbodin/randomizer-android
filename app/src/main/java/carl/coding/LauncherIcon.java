package carl.coding;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Shows the launcher icon that matches the app theme. The manifest declares one
 * launcher activity-alias per icon, and exactly one of them is enabled.
 */
final class LauncherIcon {
    private static final String LIGHT = "carl.coding.LauncherLight";
    private static final String DARK = "carl.coding.LauncherDark";

    private LauncherIcon() {
    }

    static void apply(Context context, boolean dark) {
        // Enable the new entry before disabling the old one, so one is always present.
        setEnabled(context, dark ? DARK : LIGHT, true);
        setEnabled(context, dark ? LIGHT : DARK, false);
    }

    private static void setEnabled(Context context, String alias, boolean enabled) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName component = new ComponentName(context, alias);
        int state = enabled
                ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        // Changing the state makes the launcher reload its entries, so skip no-op writes.
        if (packageManager.getComponentEnabledSetting(component) != state) {
            packageManager.setComponentEnabledSetting(component, state, PackageManager.DONT_KILL_APP);
        }
    }
}
