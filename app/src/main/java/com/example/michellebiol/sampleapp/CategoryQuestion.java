package com.example.michellebiol.sampleapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellebiol.sampleapp.Adapters.QuestionsAdapter;
import com.example.michellebiol.sampleapp.Interfaces.IAnswerApi;
import com.example.michellebiol.sampleapp.Interfaces.IQuestionByCategoryApi;
import com.example.michellebiol.sampleapp.LifeModule.Life;
import com.example.michellebiol.sampleapp.Models.AnswerRequest;
import com.example.michellebiol.sampleapp.Models.AnswerResponse;
import com.example.michellebiol.sampleapp.Models.QuestionsItem;
import com.example.michellebiol.sampleapp.Models.QuestionsResponse;
import com.example.michellebiol.sampleapp.PointModule.Points;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryQuestion extends AppCompatActivity {

    private RecyclerView questionRecyclerView;
    private RecyclerView.Adapter adapter;
    private List<QuestionsItem> questionsItems;
    private static final long counter = 21000;
    CountDownTimer countDownTimer;
    IQuestionByCategoryApi service;
    IAnswerApi answerService;
    TextView questionId , question , timerCountDown , questionResult , questionFunFacts , userCurrentLife;
    RadioGroup RGroup;
    RadioButton choice_a ,choice_b , choice_c , choice_d , radioButton;
    private long mLastClickTime = 0;
    private String correctAnswer , funFacts;
    private LinearLayout displayResult , questionLayout;
    Button btnNext,btnShareOnFB;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Life life;
    Points p;
    boolean isCounterRunning;
    private int userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_question);

        questionId = (TextView) findViewById(R.id.questionId);
        question = (TextView) findViewById(R.id.question);
        questionResult = (TextView) findViewById(R.id.questionResult);
        questionFunFacts = (TextView) findViewById(R.id.questionFunFacts);
        timerCountDown = (TextView) findViewById(R.id.timerCountDown);
        userCurrentLife = (TextView) findViewById(R.id.userCurrentLife);

        choice_a = (RadioButton) findViewById(R.id.choice_a);
        choice_b = (RadioButton) findViewById(R.id.choice_b);
        choice_c = (RadioButton) findViewById(R.id.choice_c);
        choice_d = (RadioButton) findViewById(R.id.choice_d);
        RGroup = (RadioGroup) findViewById(R.id.RGroup);
        displayResult = (LinearLayout) findViewById(R.id.displayResult);
        questionLayout = (LinearLayout) findViewById(R.id.questionLayout);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnShareOnFB = (Button) findViewById(R.id.btnShareOnFB);
        life = new Life(this);
        p = new Points(this);


        //initialize retrofit for answer api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        answerService = retrofit.create(IAnswerApi.class);

        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //trigger for counter or timer
        isCounterRunning = false;

        btnShareOnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnShareOnFB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {
                                Toast.makeText(CategoryQuestion.this, "Successfully share!", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(CategoryQuestion.this, "Share cancel", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(CategoryQuestion.this, error.getMessage(), Toast.LENGTH_SHORT)
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
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.start();
                displayNewQuestion();
            }
        });
    }


    private void getQuestions()
    {

        SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");
        questionsItems = new ArrayList<>();

        Intent i = getIntent();
        String id = i.getStringExtra("category_id");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.user_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IQuestionByCategoryApi.class);

        Call<List<QuestionsResponse>> call = service.getQuestions(token_type+token,Integer.parseInt(id));

        call.enqueue(new Callback<List<QuestionsResponse>>() {

            @Override
            public void onResponse(Call<List<QuestionsResponse>> call, Response<List<QuestionsResponse>> response) {
                List<QuestionsResponse> questions = response.body();
                for(QuestionsResponse q: questions)
                {
                    QuestionsItem questionsItem = new QuestionsItem(
                            q.getId(),q.getQuestion(),q.getQuestion_categories_id(),
                            q.getChoice_a(),q.getChoice_b(),q.getChoice_c(),q.getChoice_d(),q.getCorrect_answer(),
                            q.getFun_facts(),q.getQuestion_result());
                    questionsItems.add(questionsItem);
                }

//                adapter = new QuestionsAdapter(questionsItems,getApplicationContext());
//                questionRecyclerView.setAdapter(adapter);
                if(response.isSuccessful())
                {
                    setQuestion();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionsResponse>> call, Throwable t) {
                //add some custom message for exception
            }
        });

    }

    @Override
    protected void onResume()
    {
        getQuestions();
        super.onResume();
    }

    private List<QuestionsItem> generateRandomQuestion(List<QuestionsItem> arr , int n)
    {
        Random r = new Random();

        for(int i = n-1; i > 0; i--)
        {
            int j = r.nextInt(i);

            QuestionsItem temp = arr.get(i);
            arr.set(i, arr.get(j));
            arr.set(j, temp);
        }

        return arr;
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

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }



    private void setQuestion()
    {
        int size = questionsItems.size();
        if (size == 0){
            Toast.makeText(this, "congratulations you answered all the questions", Toast.LENGTH_SHORT).show();
            return;
        } else {
            QuestionsItem randQuestion = generateRandomQuestion(questionsItems, size).get(0);
            String[] arr = {
                    randQuestion.getChoice_a(),
                    randQuestion.getChoice_b(),
                    randQuestion.getChoice_c(),
                    randQuestion.getChoice_d()
            };
            int n = arr.length;
            String[] shuffledChoices = randomize(arr,n);
            startTimer(counter);
            userCurrentLife.setText(life.setUserLife());
            questionId.setText(randQuestion.getId());
            question.setText(randQuestion.getQuest());
            correctAnswer = randQuestion.getCorrect_answer();
            funFacts = randQuestion.getFun_facts();
            choice_a.setText(shuffledChoices[0]);
            choice_b.setText(shuffledChoices[1]);
            choice_c.setText(shuffledChoices[2]);
            choice_d.setText(shuffledChoices[3]);
        }

    }





    public void getSelected(View v)
    {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        int selectedId = RGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        countDownTimer.cancel();
        RGroup.clearCheck();
        displayResult(isCorrect(radioButton.getText().toString()));
    }

    private void displayResult(String correctOrWrong) {
        questionLayout.setVisibility(View.INVISIBLE);
        questionResult.setText("Result : " + correctOrWrong);
        questionFunFacts.setText("Fun facts : " + funFacts);
        displayResult.setVisibility(View.VISIBLE);
        getQuestions();
        Toast.makeText(this, "Points : " + String.valueOf(p.getPoints()), Toast.LENGTH_SHORT).show();

    }

    private void displayNewQuestion() {
        questionLayout.setVisibility(View.VISIBLE);
        displayResult.setVisibility(View.INVISIBLE);
    }

    private void timesUpCheck(RadioGroup RGroup)
    {
        int selectedId = RGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        String getResult = null;
        if(selectedId <= -1) {
            getResult = isCorrect("No Answer");
            Toast.makeText(this, "Message Result : " + life.questionResult(getResult,userCurrentLife.getText().toString()), Toast.LENGTH_SHORT).show();
        } else {
            getResult = isCorrect(radioButton.getText().toString());

        }
        countDownTimer.cancel();
        displayResult(getResult);
        RGroup.clearCheck();
    }

    //send the user status to the database using retrofit
    private void sendAnswer(String questionId, String result)
    {
        //get the user token
        SharedPreferences sharedPref = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        String token_type = sharedPref.getString("token_type","");
        String token = sharedPref.getString("token","");

        final AnswerRequest answerRequest = new AnswerRequest();
        answerRequest.setQuestion_id(Integer.parseInt(questionId));
        answerRequest.setQuestion_result(result);

        Call<AnswerResponse> answerResponseCall = answerService.userAnswer(token_type.concat(token),answerRequest);
        answerResponseCall.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {

            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                Toast.makeText(CategoryQuestion.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //check if correct or not
    private String isCorrect(String selectedAnswer)
    {
        String result = null;
        if(selectedAnswer.equals(correctAnswer.trim()))
        {
            result = "Correct";
            p.setPoints(++userPoints);
            sendAnswer(questionId.getText().toString(),result);
        }
        else
        {
            result = "Wrong";
            if( life.questionResult(result,userCurrentLife.getText().toString()).equals("Game over")) {
                p.insertPoints(p.getPoints());
                userPoints = 0;
            }
        }
        return result;
    }


    private  void startTimer(final long counter) {

       if (!isCounterRunning) {
           isCounterRunning  = true;
           countDownTimer = new CountDownTimer(counter, 1000) {
               @Override
               public void onTick(long millisUntilFinished) {
                   timerCountDown.setText(
                           String.format("%02d",
                                   TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

               }

               @Override
               public void onFinish() {
                   isCounterRunning = false;
                   timesUpCheck(RGroup);
               }
           };
           countDownTimer.start();
       } else {
           countDownTimer.cancel();
           countDownTimer.start();
       }
   }



}
