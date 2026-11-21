package carl.coding;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertical list of text inputs, each with a Remove button, inside a container
 * layout. Used by the setup screens to collect categories and players.
 */
final class EditableList {

    private final LinearLayout container;
    private final String hint;
    private final int inputType;

    EditableList(LinearLayout container, String hint, int inputType) {
        this.container = container;
        this.hint = hint;
        this.inputType = inputType;
    }

    /** Shows the saved values, or {@code emptyRows} blank rows when there are none. */
    void show(List<String> saved, int emptyRows) {
        container.removeAllViews();
        if (saved.isEmpty()) {
            for (int i = 0; i < emptyRows; i++) {
                addRow(null, false);
            }
        } else {
            for (String value : saved) {
                addRow(value, false);
            }
        }
    }

    void addRow(String prefill, boolean requestFocus) {
        View row = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_editable_row, container, false);
        EditText input = row.findViewById(R.id.row_input);
        input.setHint(hint);
        input.setInputType(inputType);
        input.setText(prefill);
        // Done/Enter moves to the next row, adding one after the last, so a whole
        // list can be typed in one go.
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE && event == null) {
                focusNextRow(row);
                return true;
            }
            return false;
        });
        // Enter key events (hardware keyboards, some IMEs) are handled on key up and
        // consumed, otherwise the key up would reach the next row and move focus again.
        input.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode != KeyEvent.KEYCODE_ENTER) {
                return false;
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                focusNextRow(row);
            }
            return true;
        });
        row.findViewById(R.id.row_remove).setOnClickListener(v -> container.removeView(row));

        container.addView(row);
        if (requestFocus) {
            input.requestFocus();
        }
    }

    private void focusNextRow(View row) {
        int next = container.indexOfChild(row) + 1;
        if (next < container.getChildCount()) {
            inputAt(next).requestFocus();
        } else {
            addRow(null, true);
        }
    }

    /** Trimmed, non-empty values in display order. */
    ArrayList<String> values() {
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            String value = inputAt(i).getText().toString().trim();
            if (!TextUtils.isEmpty(value)) {
                values.add(value);
            }
        }
        return values;
    }

    void removeEmptyRows() {
        for (int i = container.getChildCount() - 1; i >= 0; i--) {
            if (TextUtils.isEmpty(inputAt(i).getText().toString().trim())) {
                container.removeViewAt(i);
            }
        }
    }

    private EditText inputAt(int index) {
        return container.getChildAt(index).findViewById(R.id.row_input);
    }
}
