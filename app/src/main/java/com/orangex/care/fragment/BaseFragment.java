package com.orangex.care.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    
    // TODO: Rename and change types of parameters
    private String mParam1;
    //
    //    private OnFragmentInteractionListener mListener;
    protected Activity mActivity;
    
    public BaseFragment() {
        // Required empty public constructor
    }
    
    public static <T extends BaseFragment> T newInstance(Class<T> cls, String param1) {
        T fragment = null;
        try {
            fragment = cls.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        if (fragment != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }
    
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        return view;
    }
    
    protected abstract void initView(View view, Bundle savedInstanceState);
    
    protected abstract int getLayoutID();
    
    //    // TODO: Rename method, update argument and hook method into UI event
    //    public void onButtonPressed(Uri uri) {
    //        if (mListener != null) {
    //            mListener.onFragmentInteraction(uri);
    //        }
    //    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        //        mListener = null;
    }
    
    
}
