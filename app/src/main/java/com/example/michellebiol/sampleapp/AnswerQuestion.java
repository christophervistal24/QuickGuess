package com.example.michellebiol.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Adapters.QuestionsAdapter;
import com.example.michellebiol.sampleapp.Interfaces.IAnswerApi;
import com.example.michellebiol.sampleapp.Models.AnswerRequest;
import com.example.michellebiol.sampleapp.Models.AnswerResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsItem;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnswerQuestion extends AppCompatActivity {

    TextView givenQuestionId,
            givenQuestion ,
            correctAnswer ,
            timerCountDown ,
            questionFunFacts ,
            answerResult ,
            currentQuestionAndTotal;
    RadioButton choiceA , choiceB , choiceC , choiceD , radioButton;
    RadioGroup RGroup;
    private static final int counter = 20;
    CountDownTimer countDownTimer;
    Button confirmQuestion ,
            nextStage ,
            btnShareOnFacebook;
    IAnswerApi services;
    Intent i;
    static int intPosition;
    QuestionsItem question;
    LinearLayout questionLayout;
    LinearLayout questionResult;
    CallbackManager callbackManager;
    ShareDialog shareDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        givenQuestionId = (TextView) findViewById(R.id.givenQuestionId);
        givenQuestion = (TextView) findViewById(R.id.givenQuestion);
        correctAnswer = (TextView) findViewById(R.id.correctAnswer);
        questionFunFacts = (TextView) findViewById(R.id.questionFunFacts);
        answerResult = (TextView) findViewById(R.id.answerResult);
        currentQuestionAndTotal = (TextView) findViewById(R.id.currentQuestionAndTotal);

        choiceA = (RadioButton) findViewById(R.id.choice_a);
        choiceB = (RadioButton) findViewById(R.id.choice_b);
        choiceC = (RadioButton) findViewById(R.id.choice_c);
        choiceD = (RadioButton) findViewById(R.id.choice_d);
        RGroup  = (RadioGroup) findViewById(R.id.RGroup);

        questionLayout = (LinearLayout) findViewById(R.id.questionLayout);
        questionResult = (LinearLayout) findViewById(R.id.questionResult);

        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        final QuestionsItem question;
        i =  getIntent();

        timerCountDown = (TextView) findViewById(R.id.timerCountDown);

        confirmQuestion     = (Button) findViewById(R.id.confirmQuestion);
        nextStage            = (Button) findViewById(R.id.nextStage);
        btnShareOnFacebook  = (Button) findViewById(R.id.btnShareOnFacebook);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit.create(IAnswerApi.class);

        setTimer(counter);

        setAndGetQuestionAnswer();

        nextStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedToNextQuestion();
                resumeAnswering();
            }
        });

        btnShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                   @Override
                   public void onSuccess(Sharer.Result result) {
                       Toast.makeText(AnswerQuestion.this, "Successfully share!", Toast.LENGTH_SHORT)
                               .show();
                   }

                   @Override
                   public void onCancel() {
                       Toast.makeText(AnswerQuestion.this, "Share cancel", Toast.LENGTH_SHORT)
                               .show();
                   }

                   @Override
                   public void onError(FacebookException error) {
                       Toast.makeText(AnswerQuestion.this, error.getMessage(), Toast.LENGTH_SHORT)
                               .show();
                   }
               });

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("We are still working on it.")
                        .setContentUrl(Uri.parse("https://quick-guess.herokuapp.com/"))
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#Quick-Guess Fun Facts")
                                .build())
                        .build();

                if (ShareDialog.canShow(ShareLinkContent.class))
                {
                    shareDialog.show(linkContent);
                }
            }
        });

    }

  private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.michellebiol.sampleapp",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void setTimer(Integer counter) {

        countDownTimer = new CountDownTimer(counter * 1000,1000) {
            public void onTick(long millisUntilFinished) {
                    timerCountDown.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
                timerCountDown.setText("0");
                timesUpCheck();
            }

        }.start();
    }

    //get the question info.
    private void getQuestionInformation()
    {
        currentQuestionAndTotal.setText("Question : " + String.valueOf(intPosition+1) + " / " + QuestionsAdapter.questionsItems.size());
        givenQuestionId.setText("Question ID : " + question.getId());

        givenQuestion.setText("Question : " + question.getQuest());

        String[] arr = {
                question.getChoice_a(),
                question.getChoice_b(),
                question.getChoice_c(),
                question.getChoice_d()
        };

        int n = arr.length;

        String[] shuffledChoices = randomize(arr,n);

        choiceA.setText(shuffledChoices[0]);
        choiceB.setText(shuffledChoices[1]);
        choiceC.setText(shuffledChoices[2]);
        choiceD.setText(shuffledChoices[3]);
    }

    private void setAndGetQuestionAnswer() {
        String position = i.getStringExtra("current_position");

        intPosition = Integer.parseInt(position);

        question = QuestionsAdapter.questionsItems.get(intPosition);

        getQuestionInformation();

        confirmQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelected();
            }
        });

    }

    //randomize the choices
    public String[] randomize(String arr[] , int n)
    {
        Random r = new Random();

        for(int i = n-1; i > 0; i--)
        {
            int j = r.nextInt(i);

            String temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        return arr;
    }


    private void proceedToNextQuestion()
    {
        intPosition++;
        countDownTimer.cancel();
        isFinish(intPosition);
        getQuestionInformation();
        RGroup.clearCheck();
        setTimer(counter);


    }

    //get the selected answer of the user
    public void getSelected()
    {
        int radioId = RGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if (radioId <= -1)
        {
            Toast.makeText(this, "Please select some answer", Toast.LENGTH_SHORT).show();
        }
        else
        {

            answerResult.setText( "Results : " + isCorrect(radioButton.getText().toString()));
            displayFunFacts();
        }
    }

    private void displayResult()
    {
        nextStage.setVisibility(View.VISIBLE);
        btnShareOnFacebook.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        questionLayout.setVisibility(View.INVISIBLE);
        questionResult.setVisibility(View.VISIBLE);
    }


    private void resumeAnswering()
    {
        nextStage.setVisibility(View.INVISIBLE);
        btnShareOnFacebook.setVisibility(View.INVISIBLE);
        questionLayout.setVisibility(View.VISIBLE);
        questionResult.setVisibility(View.INVISIBLE);
        countDownTimer.start();

    }

    // if the question category is already finish
    private void isFinish(int intPosition)
    {

        if (intPosition+1 <= QuestionsAdapter.questionsItems.size())
        {
            question = QuestionsAdapter.questionsItems.get(intPosition);
        } else
        {
            currentQuestionAndTotal.setVisibility(View.INVISIBLE);
            this.finish();
        }
    }


    private String isCorrect(String selectedAnswer)
    {

        String result = null;
        if(selectedAnswer.equals(question.getCorrect_answer()))
        {
            result =  "Correct";
            sendAnswer(result);
            displayResult();
            return result;
        }
        else
        {
            result = "Wrong";
            sendAnswer(result);
            displayResult();
            return result;
        }
    }

    private void timesUpCheck()
    {
        int radioId = RGroup.getCheckedRadioButtonId();
        if (radioId <= -1)
        {
            Toast.makeText(this,  "Result : " + isCorrect("No Answer"), Toast.LENGTH_SHORT).show();
            displayFunFacts();


        }else {
            Toast.makeText(this, "Result : " + isCorrect(radioButton.getText().toString()), Toast.LENGTH_SHORT).show();
            displayFunFacts();
        }
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }


    //display the fun facts of the question that the user choose
    private void displayFunFacts()
    {
        questionFunFacts.setText("Fun facts :" + question.getFun_facts());
        displayResult();

    }

    //send the user status to the database using retrofit
    private void sendAnswer(String result)
    {
        //get the user token &
        SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");

        final AnswerRequest answerRequest = new AnswerRequest();
        answerRequest.setQuestion_id(Integer.parseInt(question.getId()));
        answerRequest.setQuestion_result(result);

        Call<AnswerResponse> answerResponseCall = services.userAnswer(token_type.concat(token),answerRequest);
        answerResponseCall.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {

            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                Toast.makeText(AnswerQuestion.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
