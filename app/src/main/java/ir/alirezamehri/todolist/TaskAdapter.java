package ir.alirezamehri.todolist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private TaskItemEventListener eventListener;

    public TaskAdapter(TaskItemEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    public void addItem(Task task) {
        tasks.add(0, task);
        notifyItemInserted(0);
    }

    public void addItems(List<Task> tasks) {
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void removeItem(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void updateItem(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void clearAllItems() {
        tasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private View deleteBtn;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_box_task);
            deleteBtn = itemView.findViewById(R.id.btn_task_delete);
        }

        public void bind(Task task) {
            checkBox.setOnCheckedChangeListener(null); // for error onCheckedChanged
            checkBox.setChecked(task.isCompleted());
            checkBox.setText(task.getTitle());
            deleteBtn.setOnClickListener(v -> {
                eventListener.onDeleteButtonClick(task);
            });
            checkBox.setOnLongClickListener(v -> {
                eventListener.onItemLongPress(task);
                return false;
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    task.setCompleted(b);
                    eventListener.onItemCheckedChange(task);
                }
            });
        }
    }

    public interface TaskItemEventListener {
        void onDeleteButtonClick(Task task);

        void onItemLongPress(Task task);

        void onItemCheckedChange(Task task);
    }
}
