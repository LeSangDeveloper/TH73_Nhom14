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
public class FifthFragment extends Fragment {

    private TextView next, back;
    private ViewPager viewPager;

    public FifthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fifth,container,false);
        // Inflate the layout for this fragment

        viewPager = getActivity().findViewById(R.id.viewPager);
        next = (TextView)view.findViewById(R.id.next);
        back = (TextView)view.findViewById(R.id.back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(5);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(3);
            }
        });

        return view;
    }
}
