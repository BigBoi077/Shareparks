package cegepst.example.shareparks.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.Post;

public class FeedFragment extends Fragment {

    private FeedAdapter feedAdapter;
    private ArrayList<Post> posts;

    public static FeedFragment newInstance(ArrayList<Post> posts) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("posts", posts);
        feedFragment.setArguments(args);
        return feedFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.posts = getArguments().getParcelableArrayList("posts");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView listView = view.findViewById(R.id.feedList);
        feedAdapter = new FeedAdapter(posts);
        listView.setAdapter(feedAdapter);
        listView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}
