package com.lifeistech.android.tictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //プレイヤーの数
    private static final int PLAYER_COUNT = 2;
    //それぞれのプレイヤーがボードに置く画像
    private static final int[] PLAYER_IMAGES = {R.drawable.ic_batsu,R.drawable.ic_maru};
    //ターン数を数える関数
    //プレイヤーも管理するプレイヤー1は1、プレイヤー2は0
    private int mTurn;
    //ゲームの盤面
    //誰もまだ選択していない時は-1
    private int [] mGameBoard;
    //実際に見えているゲームの盤面：ボタンの配列
    private ImageButton[] mBoardButtons;
    //プレイヤーとターン表示用のTextView
    private TextView mPlayerTextView;
    //勝敗表示用のTextView
    private TextView mWinnerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextViewの紐付け
        mPlayerTextView = (TextView) findViewById(R.id.playerText);
        mWinnerTextView = (TextView) findViewById(R.id.winnerText);

        //ImageButtonの紐付け
        mBoardButtons = new ImageButton[9];
        int[] buttonIds = {
                R.id.imageButton1,
                R.id.imageButton2,
                R.id.imageButton3,
                R.id.imageButton4,
                R.id.imageButton5,
                R.id.imageButton6,
                R.id.imageButton7,
                R.id.imageButton8,
                R.id.imageButton9};
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i] = (ImageButton) findViewById(buttonIds[i]);
            mBoardButtons[i].setTag(new Integer(i));
        }

        //ゲームの初期化をするメソッド呼び出し
        init();
        //現在のプレイヤーをセットするメソッドの呼び出し
        setPlayer();
    }


    private void init() {
        //変数の初期化
        mTurn = 1;
        //現在のボードゲームの初期化
        mGameBoard = new int[mBoardButtons.length];
        for (int i = 0; i < mBoardButtons.length; i++) {
                //誰もそのますを取っていない時は、-1が入っているようにする
                mGameBoard[i] = -1;
                //ImageButtonの方も表示している画像を消す
                mBoardButtons[i].setImageBitmap(null);
        }

        //勝敗の表示用のTextViewは見えないようにしておく
        mWinnerTextView.setVisibility(View.GONE);
    }


    private void setPlayer() {
        if (mTurn % 2 == 0) {
            mPlayerTextView.setText("Player: ×(2)");
        } else {
            mPlayerTextView.setText("Player: ○(1)");
        }
    }

    public void tapMark(View v) {
        //勝敗画面が出ていない時だけ処理を行うようにする
        if (mWinnerTextView.getVisibility() == View.VISIBLE) return;
        //どのボタンが押されたのかを取得する
        int buttonNumber =(Integer) v.getTag();
        //まだ誰もそのマスを取っていないことを確認する
        if (mGameBoard[buttonNumber] == -1) {
            //そのターンのプレイヤーの画像を押されたマスにセットする
            mBoardButtons[buttonNumber].setImageResource(PLAYER_IMAGES[mTurn % 2]);
            mGameBoard[buttonNumber] = mTurn % 2;
            //勝敗がついたかを判定する
            int judge = judgeGame();
            //judgeの値が-1なら、勝敗がついていない
            //judgeの値が1なら、○のプレイヤーの勝利
            //judgeの値が0なら、×のプレイヤーの勝利
            if (judge != -1) {
                //勝敗がついた場合
                if (judge == 0) {
                    mWinnerTextView.setText("Game End\nPlayer: ×(2)\nWin");
                    mWinnerTextView.setTextColor(Color.BLUE);
                } else {
                    mWinnerTextView.setText("Game End\nPlayer: ○(1)\nWin");
                    mWinnerTextView.setTextColor(Color.RED);
                }
                mWinnerTextView.setVisibility(View.VISIBLE);
            } else {
                //全部の場所に置いて、決着がつかなかった場合を確認する
                if (mTurn >= mGameBoard.length) {
                    mWinnerTextView.setText("Game End\nDraw");
                    mWinnerTextView.setTextColor(Color.YELLOW);
                    mWinnerTextView.setVisibility(View.VISIBLE);
                }
                //次のターン
                mTurn++;
                setPlayer();
            }
        }
    }

    public int judgeGame() {
        for (int i = 0;i < 3;i++) {
            //横の並びをチェック
            if (mGameBoard[i * 3] !=-1 &&
                    mGameBoard[i * 3] == mGameBoard[i * 3 + 1] &&
                    mGameBoard[i * 3] == mGameBoard[i * 3 + 2]) {
                return mGameBoard[i * 3];
            }
            //縦の並びをチェック
            if (mGameBoard[i] !=-1 &&
                    mGameBoard[i] == mGameBoard[i + 3] &&
                    mGameBoard[i] == mGameBoard[i + 6]) {
                return mGameBoard[i];
            }
        }
        //斜めの並びチェック
        //左上から右下
        if (mGameBoard[0] !=-1 &&
                mGameBoard[0] == mGameBoard[4] &&
                mGameBoard[0] == mGameBoard[8]) {
            return mGameBoard[0];
        }
        //右上から左下
        if (mGameBoard[2] !=-1 &&
                mGameBoard[2] == mGameBoard[4] &&
                mGameBoard[2] == mGameBoard[6]) {
            return mGameBoard[2];
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            //ゲームの初期化処理を呼び出し
            init();
            setPlayer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
