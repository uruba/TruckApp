package cz.uruba.ets2mpcompanion.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import cz.uruba.ets2mpcompanion.R;
import cz.uruba.ets2mpcompanion.adapters.ColourChooserAdapter;
import cz.uruba.ets2mpcompanion.constants.Themes;
import cz.uruba.ets2mpcompanion.views.ColourChooserView;

public class ColourChooserPreference extends ListPreference implements AdapterView.OnItemClickListener {

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

        ColourChooserView colourGrid = (ColourChooserView) view.findViewById(R.id.dialog_colours);

        ColourChooserAdapter adapter = new ColourChooserAdapter(getContext(), Themes.getThemeList());

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
        getDialog().dismiss();
    }
}
