package com.easternblu.khub.ktr.view;

import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.easternblu.khub.ktr.MainThreadScheduler;
import com.easternblu.khub.ktr.model.Line;
import com.easternblu.khub.ktr.model.Lines;

import java.util.UUID;

import kotlin.jvm.Synchronized;

import static com.easternblu.khub.ktr.view.State.DONE;


public class LineLayout extends LinearLayout {


    private String scheduleId = null;
    private State state = State.NOT_INIT;
    private Line current;
    private Line previous;

    public LineLayout(@NonNull Context context) {
        super(context);
        setupView();
    }

    public LineLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public LineLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupView();
    }

    private void setupView() {
    }

    public Line getPrevious() {
        return previous;
    }

    public void setPrevious(Line previous) {
        this.previous = previous;
    }

    public Line getCurrent() {
        return current;
    }

    public void setCurrent(Line current) {
        this.current = current;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void reset() {
        cancelSweeping();
        setPrevious(null);
        setCurrent(null);
        setState(State.NOT_INIT);
        removeAllViews();
    }


    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Synchronized
    public void cancelSweeping() {
        for (int i = 0; i < this.getChildCount(); i++) {
            View v;
            SweepingStrokeTextViewLayout stv;
            if ((v = this.getChildAt(i)) instanceof SweepingStrokeTextViewLayout) {
                stv = (SweepingStrokeTextViewLayout) v;
                stv.updateAsDone();
                stv.cancelScheduledSweeping();
            }
        }
    }


    @MainThread
    public void clearAfter(MainThreadScheduler scheduler, long delay, final Utils.Lambda<Void, Lines> getLines) {
        final String autoHideScheduleId = UUID.randomUUID().toString();
        setScheduleId(autoHideScheduleId);
        final Lines lines = getLines.invoke(null);
        scheduler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Lines _lines = getLines.invoke(null);
                if (_lines == null || !lines.equals(_lines)) {
                    return;
                }
                //Timber.d("ktr_schedule_id = " + lineLayout.getTag(R.id.ktr_schedule_id) + ", autoHideScheduleId = " + autoHideScheduleId);
                if (getState() == DONE && Utils.isNotNullAndEquals(autoHideScheduleId, getScheduleId())) {
                    setVisibility(INVISIBLE);
                }
            }
        }, delay);
    }

}
