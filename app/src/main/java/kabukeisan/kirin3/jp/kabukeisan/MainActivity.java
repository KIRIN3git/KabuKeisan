package kabukeisan.kirin3.jp.kabukeisan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEditMeigara, mEditShutokuKabuka, mEditShutokuKabusuu, mEditShutokuKingaku;
    private TextView mTextSoneki, mTextHaito, mTextGoukeiSoneki, mTextKingaku;

    private CustomNumberPicker mNumPickerKabuka, mNumPickerNensuu;

    // 株価ナンバーピッカーの表示範囲最大倍率(*4)、最小倍率(/4)
    final Integer NUMBER_PICKER_KABUKA_RANGE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditMeigara = (EditText) findViewById(R.id.editShutokuKabuka);
        mEditShutokuKabuka = (EditText) findViewById(R.id.editShutokuKabuka);
        mEditShutokuKabusuu = (EditText) findViewById(R.id.editShutokuKabusuu);
        mEditShutokuKingaku = (EditText) findViewById(R.id.editShutokuKingaku);

        mNumPickerKabuka = (CustomNumberPicker) findViewById(R.id.numPickerKabuka);
        mNumPickerNensuu = (CustomNumberPicker) findViewById(R.id.numPickerNensuu);

        mTextSoneki = (TextView) findViewById(R.id.textSoneki);
        mTextHaito = (TextView) findViewById(R.id.textHaito);
        mTextGoukeiSoneki = (TextView) findViewById(R.id.textGoukeiSoneki);
        mTextKingaku = (TextView) findViewById(R.id.textKingaku);

        getChangeEdit();
        getChangeNumberPicker();
    }

    public void getChangeEdit() {

        // 銘柄設定
        mEditMeigara.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.w("DEBUG_DATA", "bbbbbbbbb " + mEditMeigara.getText().toString());
                return false;
            }
        });

        // 取得株価設定
        mEditShutokuKabuka.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.w("DEBUG_DATA", "bbbbbbbbb1 " + mEditShutokuKabuka.getText().toString());

                setNumPickerKabuka(mEditShutokuKabuka.getText().toString());

                return false;
            }
        });

        // 取得株数設定
        mEditShutokuKabusuu.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.w("DEBUG_DATA", "bbbbbbbbb2 " + mEditShutokuKabuka.getText().toString());
                Log.w("DEBUG_DATA", "bbbbbbbbb2 " + mEditShutokuKabusuu.getText().toString());

                setShutokuKingaku(mEditShutokuKabuka.getText().toString(),mEditShutokuKabusuu.getText().toString());

                return false;
            }
        });

        // 取得金額設定
        mEditShutokuKingaku.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.w("DEBUG_DATA", "bbbbbbbbb " + mEditShutokuKingaku.getText().toString());

                setShutokuKabusuu(mEditShutokuKabuka.getText().toString(),mEditShutokuKingaku.getText().toString());
                return false;
            }
        });
    }


    public void getChangeNumberPicker() {

        // 変更を受け取るリスナー
        mNumPickerKabuka.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                // ( ナンバーピッカー株価（newVal + newVal/NUMBER_PICKER_KABUKA_RANGE） - 取得株価 ) * 取得株数
                Integer shutokuKabuka = Integer.parseInt(mEditShutokuKabuka.getText().toString());
                Integer editShutokuKabusuu = Integer.parseInt(mEditShutokuKabusuu.getText().toString());

                Integer soneki = ( (newVal + shutokuKabuka/NUMBER_PICKER_KABUKA_RANGE) - shutokuKabuka ) * editShutokuKabusuu;


                Log.w( "DEBUG_DATA", "aaaaaaaaa oldVal =" + oldVal);
                Log.w( "DEBUG_DATA", "aaaaaaaaa newVal =" + newVal);
                Log.w( "DEBUG_DATA", "aaaaaaaaa newVal/NUMBER_PICKER_KABUKA_RANGE =" + newVal/NUMBER_PICKER_KABUKA_RANGE);
                Log.w( "DEBUG_DATA", "aaaaaaaaa shutokuKabuka =" + shutokuKabuka);
                Log.w( "DEBUG_DATA", "aaaaaaaaa editShutokuKabusuu =" + editShutokuKabusuu);
                Log.w( "DEBUG_DATA", "aaaaaaaaa soneki =" + soneki);


                mTextSoneki.setText(String.valueOf(soneki));
            }
        });
    }

    /**
     * 取得株価、取得株数から取得金額を表示
     *
     * @param kabuka
     * @param kabusuu
     */
    public void setShutokuKingaku(String kabuka, String kabusuu){
        Long kingaku;

        if(kabuka.length() < 1 || kabusuu.length() < 1) return;

        kingaku = Long.parseLong(kabuka) * Long.parseLong(kabusuu);

        mEditShutokuKingaku.setText(String.valueOf(kingaku));
    }

    /**
     * 取得株価、取得金額から取得株数を表示
     *
     * @param kabuka
     * @param kingaku
     */
    public void setShutokuKabusuu(String kabuka, String kingaku){
        Integer kabusuu;

        if(kabuka.length() < 1 || kingaku.length() < 1) return;

        kabusuu = Integer.parseInt(kingaku) / Integer.parseInt(kabuka);

        mEditShutokuKabusuu.setText(String.valueOf(kabusuu));
    }

    /**
     * 取得株価から株価ナンバーピッカーを設定
     *
     * @param kabuka
     */
    public void setNumPickerKabuka(String kabuka){

        if(kabuka.length() < 1) return;

        Log.w( "DEBUG_DATA", "kabuka =============== " + kabuka);

        // 範囲設定
        mNumPickerKabuka.setMaxValue(Integer.parseInt(kabuka) * NUMBER_PICKER_KABUKA_RANGE);
        mNumPickerKabuka.setMinValue(Integer.parseInt(kabuka) / NUMBER_PICKER_KABUKA_RANGE);

        // 初期値設定
        mNumPickerKabuka.setValue(Integer.parseInt(kabuka) - (Integer.parseInt(kabuka) / NUMBER_PICKER_KABUKA_RANGE));
    }
}
