package com.example.broaf;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PostViewModel extends ViewModel {
    private PostLogic postLogic = new PostLogic();
    //private PostRepository postRepository;
    private SingleLiveEvent<User> createPostEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> editPostEvent = new SingleLiveEvent<>();
    private MutableLiveData<NormalPost> postLiveData;


    public SingleLiveEvent<User> getCreatePostEvent() {
        return createPostEvent;
    }
    public SingleLiveEvent<Integer> getEditPostEvent() {return  editPostEvent; }
    public void onCreatePostEventStart(User user) {
        NormalPost normalpost = new NormalPost(user.getUID(), user.getuname());
    }
}