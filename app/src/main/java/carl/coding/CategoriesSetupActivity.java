package carl.coding;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class CategoriesSetupActivity extends BaseActivity {

    private static final String KEY_CATEGORIES = "categories_list";
    private static final String KEY_CATEGORIES_WITHDRAW = "categories_withdraw";

    private EditableList categories;
    private CompoundButton withdrawSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_setup);

        categories = new EditableList(findViewById(R.id.element_list_layout), getString(R.string.categories_hint),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        withdrawSwitch = findViewById(R.id.withdraw_switch);

        findViewById(R.id.add_button).setOnClickListener(v -> categories.addRow(null, true));
        findViewById(R.id.start_button).setOnClickListener(v -> startCategories());

        withdrawSwitch.setChecked(PreferencesHelper.getBoolean(this, KEY_CATEGORIES_WITHDRAW, false));
        categories.show(PreferencesHelper.getStringList(this, KEY_CATEGORIES), 3);
    }

    private void startCategories() {
        categories.removeEmptyRows();
        persistState();

        ArrayList<String> values = categories.values();
        if (values.isEmpty()) {
            categories.addRow(null, true);
            return;
        }

        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putStringArrayListExtra(CategoriesActivity.EXTRA_ELEMENTS, values);
        intent.putExtra(CategoriesActivity.EXTRA_WITHDRAW_MODE, withdrawSwitch.isChecked());
        startActivity(intent);
    }

    private void persistState() {
        PreferencesHelper.setStringList(this, KEY_CATEGORIES, categories.values());
        PreferencesHelper.setBoolean(this, KEY_CATEGORIES_WITHDRAW, withdrawSwitch.isChecked());
    }

    @Override
    protected void onPause() {
        persistState();
        super.onPause();
    }
}
