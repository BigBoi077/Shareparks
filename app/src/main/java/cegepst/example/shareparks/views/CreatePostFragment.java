package cegepst.example.shareparks.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cegepst.example.shareparks.R;

public class CreatePostFragment extends Fragment {

    private static final String ARG_AUTHOR = "author";
    private String author;

    public static CreatePostFragment newInstance(String author) {
        Bundle args = new Bundle();
        CreatePostFragment createPostFragment = new CreatePostFragment();
        args.putString(ARG_AUTHOR, author);
        createPostFragment.setArguments(args);
        return createPostFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            author = getArguments().getString(ARG_AUTHOR);
        } else {
            author = "John Doe";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
