package cegepst.example.shareparks.views;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.Post;

public class CreatePostFragment extends Fragment {

    private static final String ARG_AUTHOR = "author";
    private Bitmap image;
    private String author;
    private String caption;

    public static CreatePostFragment newInstance(String author) {
        Bundle args = new Bundle();
        CreatePostFragment createPostFragment = new CreatePostFragment();
        args.putString(ARG_AUTHOR, author);
        createPostFragment.setArguments(args);
        return createPostFragment;
    }

    public void passImage(Bitmap image) {
        this.image = image;
        ImageView imageView = getView().findViewById(R.id.imageView);
        imageView.setImageBitmap(image);
    }

    public void getDescription() {
        EditText captionInput = getView().findViewById(R.id.captionInput);
        caption = captionInput.getText().toString();
    }

    public Post makePost() {
        Post post = new Post();
        post.setAuthor(author);
        post.setImage(image);
        post.setCaption(caption);
        post.setNbrLikes(new Random().nextInt(10000));
        return post;
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
