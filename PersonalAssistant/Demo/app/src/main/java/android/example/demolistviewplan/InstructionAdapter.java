package android.example.demolistviewplan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class InstructionAdapter extends FragmentPagerAdapter {
    public InstructionAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            case 3:
                return new FourthFragment();
            case 4:
                return new FifthFragment();
            case 5:
                return new SixthFragment();
            case 6:
                return new SeventhFragment();
            case 7:
                return new EightFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 8;
    }
}
