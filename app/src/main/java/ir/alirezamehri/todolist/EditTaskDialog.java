package ir.alirezamehri.todolist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditTaskDialog extends DialogFragment {
    private EditNewTaskCallback callback;
    private Task task;
    private String title;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (EditNewTaskCallback) context;
        task = getArguments().getParcelable("task");
        if (task == null) {
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_dialog_task, null, false);
        final TextInputEditText titleEt = view.findViewById(R.id.et_dialogEdit_title);
        titleEt.setText(task.getTitle());
        final TextInputLayout inputLayout = view.findViewById(R.id.etl_dialogEdit_title);
        View saveBtn = view.findViewById(R.id.btn_dialogEdit_save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEt.length() > 0) {
                    task.setTitle(titleEt.getText().toString());
                    callback.onEditTask(task);
                    dismiss();
                }else {
                    inputLayout.setError("عنوان را وارد کنید");
                }
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public interface EditNewTaskCallback {
        void onEditTask(Task task);
    }
}
