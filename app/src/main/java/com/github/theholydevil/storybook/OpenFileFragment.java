package com.github.theholydevil.storybook;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

enum OpenFileFragmentState {FILE, DIRECTORY}

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenFileFragment extends Fragment {
    String currentPath;
    ArrayList<String> directoryList;
    ArrayAdapter<String> listAdapter;
    OpenFileFragmentState state;
    ListView listView;

    public OpenFileFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(OpenFileFragmentState state) {
        OpenFileFragment f = new OpenFileFragment();
        Bundle args = new Bundle();

        switch (state) {
            case FILE:
                args.putBoolean("file", true);
                break;
            case DIRECTORY:
                args.putBoolean("file", false);
                break;
        }

        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().getBoolean("file"))
            this.state = OpenFileFragmentState.FILE;
        else this.state = OpenFileFragmentState.DIRECTORY;

        directoryList = new ArrayList<>();

        if(savedInstanceState == null) {

            this.currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            this.currentPath = savedInstanceState.getString("path",
                    Environment.getExternalStorageDirectory().getAbsolutePath());
        }

        File[] files = new File(this.currentPath).listFiles();

        directoryList.add("..");

        for (File file : files) {
            if (file != null && file.isDirectory()) {
                directoryList.add(file.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.file_dir_chooser, container, false);

        listView = (ListView) view.findViewById(R.id.directory_List_view);

        listAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,android.R.id.text1, directoryList);

        listAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new openDirectoryListener());

        getActivity().setTitle(R.string.choose_directory);

        view.findViewById(R.id.add_directory_button).setOnClickListener(new addBookButtonListener(getActivity()));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("path", this.currentPath);
        super.onSaveInstanceState(savedInstanceState);
    }

    private class openDirectoryListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            currentPath += "/" + directoryList.get(position);
            File[] files = new File(currentPath).listFiles();

            directoryList.clear();
            //listAdapter.clear();

            directoryList.add("..");
            //listAdapter.add("..");

            for (File file : files) {
                if (file != null && file.isDirectory()) {
                    directoryList.add(file.getName());
                    //listAdapter.add(file.getName());
                }
            }

            listAdapter.sort(new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareTo(rhs);
                }
            });

            listAdapter.notifyDataSetChanged();
        }
    }

    private class addBookButtonListener implements Button.OnClickListener {
        Context context;

        public addBookButtonListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            BookHandler.newBook(new StoryDirectoryImporter(currentPath, context));
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    BookOverview.newInstance()).commit();
        }
    }
}
