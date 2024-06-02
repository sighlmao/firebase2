package com.example.firebase2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private ListView listViewStudents;
    private List<Student> studentList;
    private FirebaseDatabaseHelper databaseHelper;
    private ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewStudents = findViewById(R.id.listViewStudents);
        studentList = new ArrayList<>();
        databaseHelper = new FirebaseDatabaseHelper();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });

        listViewStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student selectedStudent = studentList.get(position);
            editTextName.setText(selectedStudent.getName());
            editTextEmail.setText(selectedStudent.getEmail());
            editTextName.setTag(selectedStudent.getId());
        });

        loadStudents();
    }

    private void addStudent() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        if (!name.isEmpty() && !email.isEmpty()) {
            String id = databaseHelper.getReference().push().getKey();
            Student student = new Student(id, name, email);
            databaseHelper.addStudent(student);
            loadStudents(); // Update ListView
        } else {
            // Handle empty fields
        }
    }

    private void updateStudent() {
        String id = (String) editTextName.getTag();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        if (id != null && !id.isEmpty()) {
            Student student = new Student(id, name, email);
            databaseHelper.updateStudent(id, student);
            loadStudents(); // Update ListView
        } else {
            // Handle no student selected
        }
    }

    private void deleteStudent() {
        String id = (String) editTextName.getTag();
        if (id != null && !id.isEmpty()) {
            databaseHelper.deleteStudent(id);
            editTextName.setText("");
            editTextEmail.setText("");
            loadStudents(); // Update ListView
        } else {
            // Handle no student selected
        }
    }

    private void loadStudents() {
        databaseHelper.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Student student = postSnapshot.getValue(Student.class);
                    studentList.add(student);
                }
                if (adapter == null) {
                    adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, studentList);
                    listViewStudents.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
