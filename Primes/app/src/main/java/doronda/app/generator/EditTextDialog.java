package doronda.app.generator;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by doronda on 14.12.2015.
 */
public class EditTextDialog extends DialogFragment {

    private EditText mEditText;
    public static int threadCount = 1;

    public EditTextDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dialog, container);
        mEditText = (EditText) view.findViewById(R.id.edt_number_of_threads);
        mEditText.setText(String.valueOf(threadCount));
        mEditText.setSelection(mEditText.getText().toString().length());

        Button btn = (Button) view.findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEditText.getText().toString().length() == 0){
                    Toast.makeText(getActivity(), "Enter the value", Toast.LENGTH_SHORT).show();
                    return;
                } else
                try {
                    if (Integer.valueOf(mEditText.getText().toString()) < 1 || Integer.valueOf(mEditText.getText().toString()) > GeneratorActivity.NUMBER_OF_CORES * 5) {
                        Toast.makeText(getActivity(), "Enter the value from 1 to " + String.valueOf(GeneratorActivity.NUMBER_OF_CORES * 5), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException ne){
                    Toast.makeText(getActivity(), "Enter the value from 1 to " + String.valueOf(GeneratorActivity.NUMBER_OF_CORES * 5), Toast.LENGTH_SHORT).show();
                    return;
                }
                threadCount = Integer.parseInt(mEditText.getText().toString());
                dismiss();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}