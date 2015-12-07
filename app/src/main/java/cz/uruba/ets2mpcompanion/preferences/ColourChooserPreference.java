package cz.uruba.ets2mpcompanion.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.ColourChooserAdapter;
import cz.uruba.ets2mpcompanion.constants.Themes;

public class ColourChooserPreference extends ListPreference implements AdapterView.OnItemClickListener {
    private List<String> themeListKeys;

    public ColourChooserPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(), R.layout.dialog_colour_chooser, null);

        CharSequence dialogTitle = getDialogTitle();
        if (dialogTitle == null) {
            dialogTitle = getTitle();
        }
        ((TextView) view.findViewById(R.id.dialog_title)).setText(dialogTitle);

        GridView colourGrid = (GridView) view.findViewById(R.id.dialog_colours);

        Map<String, Integer> themeMap = Themes.getThemeList();
        themeListKeys = new ArrayList<>();
        List<Integer> themeListValues = new ArrayList<>();

        for (Map.Entry<String, Integer> themeStyle : themeMap.entrySet()) {
            themeListKeys.add(themeStyle.getKey());
            themeListValues.add(themeStyle.getValue());
        }

        ColourChooserAdapter adapter = new ColourChooserAdapter(getContext(), themeListValues, colourGrid);

        colourGrid.setAdapter(adapter);
        colourGrid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        colourGrid.setItemChecked(findIndexOfValue(getValue()), true);
        colourGrid.setOnItemClickListener(this);

        return view;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(null);
        builder.setPositiveButton(null, null);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onClick(getDialog(), DialogInterface.BUTTON_POSITIVE);
        setValue(themeListKeys.get(position));
        getDialog().dismiss();
    }

    // NOTE – This is dependent on the syntax of theme keys in the theme map!
    public String getValueThemeColour() {
        if (TextUtils.isEmpty(getValue())) {
            return null;
        }

        String[] words = getValue().split("(?<=\\S)(?=\\p{Upper})");

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                words[i] = words[i].toLowerCase();
            }
        }

        // Do away with the "theme" word and the first blank string (left by the split function)
        words = Arrays.copyOf(words, words.length - 1);

        return TextUtils.join(" ", words);
    }
}
