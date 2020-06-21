package android.example.demolistviewplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EightFragment extends Fragment {

    private TextView back, done;
    private ViewPager viewPager;

    public EightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eight,container,false);
        // Inflate the layout for this fragment

        viewPager = getActivity().findViewById(R.id.viewPager);
        back = (TextView)view.findViewById(R.id.back);
        done = (TextView)view.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(6);
            }
        });

        return view;
    }
}
