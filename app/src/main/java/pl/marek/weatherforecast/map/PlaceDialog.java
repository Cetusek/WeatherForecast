package pl.marek.weatherforecast.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.marek.weatherforecast.R;

public class PlaceDialog extends DialogFragment {

    private PlaceDialogListener placeDialogListener;
    private TextView nameTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_place, null);
        nameTextView = (TextView) view.findViewById(R.id.PlaceDialogText);
        Button button = (Button) view.findViewById(R.id.PlaceDialogOkButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkButtonPressed();
            }
        });
        button = (Button) view.findViewById(R.id.PlaceDialogCancelButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed();
            }
        });
        builder.setView(view);
        setNameTextView();
        return builder.create();
    }

    private void setNameTextView() {
        nameTextView.setText(getArguments().getString(MapActivity.NAME_KEY));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        placeDialogListener = (PlaceDialogListener) activity;
    }


    private void onOkButtonPressed() {
        placeDialogListener.onPlaceDialogOkPressed(nameTextView.getText().toString());
        dismiss();
    }

    private void onCancelPressed() {
        placeDialogListener.onPlaceDialogCancelPressed();
        dismiss();
    }


    public interface PlaceDialogListener {
        public void onPlaceDialogOkPressed(String name);
        public void onPlaceDialogCancelPressed();
    }

}
