package com.example.binbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Random;

public class Quiz_array extends AppCompatActivity {

    private TextView questionTV,questionNumberTV;
    private Button option1Button,option2Btn,option3Btn,option4Btn;
    private ArrayList<Games> quizModalArrayList;
    Random random;
    int currentScore = 0, questionAttempted = 1, currentPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        questionTV = findViewById(R.id.idTVQuestion);
        questionNumberTV = findViewById(R.id.idTVQuestionAttempted);
        option1Button = findViewById(R.id.idBtnOption1);
        option2Btn = findViewById(R.id.idBtnOption2);
        option3Btn = findViewById(R.id.idBtnOption3);
        option4Btn = findViewById(R.id.idBtnOption4);
        quizModalArrayList = new ArrayList<>();
        random = new Random();
        getQuizQuestion(quizModalArrayList);
        currentPos = random.nextInt(quizModalArrayList.size());
        setDataToViews(currentPos);
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option1Button.getText().toString().trim().toLowerCase())){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);

            }
        });

        option2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option2Btn.getText().toString().trim().toLowerCase())){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option3Btn.getText().toString().trim().toLowerCase())){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
            }
        });

        option4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizModalArrayList.get(currentPos).getAnswer().trim().toLowerCase().equals(option4Btn.getText().toString().trim().toLowerCase())){
                    currentScore++;
                }
                questionAttempted++;
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
            }
        });

    }

    private void showBottomSheet(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Quiz_array.this);
        View bottonSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_score_screen, (LinearLayout)findViewById(R.id.idLLScore));
        TextView scoreTv = bottonSheetView.findViewById(R.id.txt_score);
        Button restartQuizBtn = bottonSheetView.findViewById(R.id.btn_restart);
        scoreTv.setText("Your Score is \n"+currentScore + "/10");
        restartQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = random.nextInt(quizModalArrayList.size());
                setDataToViews(currentPos);
                questionAttempted = 1;
                currentScore = 0;
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottonSheetView);
        bottomSheetDialog.show();
    }

    private void setDataToViews(int currentPos){
        questionNumberTV.setText("Question Attempted : "+questionAttempted + "/10");
        if (questionAttempted == 10){
            showBottomSheet();
        }else{
            questionTV.setText(quizModalArrayList.get(currentPos).getQuestion());
            option1Button.setText(quizModalArrayList.get(currentPos).getOption1());
            option2Btn.setText(quizModalArrayList.get(currentPos).getOption2());
            option3Btn.setText(quizModalArrayList.get(currentPos).getOption3());
            option4Btn.setText(quizModalArrayList.get(currentPos).getOption4());
        }

    }
    private void getQuizQuestion(ArrayList<Games> quizModalArrayList) {
        quizModalArrayList.add(new Games("Which of the following materials is NOT recyclable?", "Aluminum cans", "Glass bottles", "Pizza boxes with grease", "Plastic containers", "Pizza boxes with grease"));
        quizModalArrayList.add(new Games("What symbol on packaging indicates that the material can be recycled?", "A green triangle", "A blue circle with an arrow", "A raised fist symbol", "A red square", "A blue circle with an arrow"));
        quizModalArrayList.add(new Games("What is the main benefit of recycling plastic?", "Reduces water consumption", "Saves energy and reduces landfill waste", "Creates new species of plastic", "Increases air pollution", "Saves energy and reduces landfill waste"));
        quizModalArrayList.add(new Games("How long does it take for a plastic bottle to decompose in a landfill?", "1 year", "10 years", "100 years", "450 years", "450 years"));
        quizModalArrayList.add(new Games("Which of the following items should NOT be placed in a recycling bin?", "Clean cardboard", "Aluminum foil", "Food containers with food residue", "Plastic water bottles", "Food containers with food residue"));
        quizModalArrayList.add(new Games("What is the process of converting old paper into new paper called?", "Pulping", "Deinking", "Composting", "Shredding", "Deinking"));
        quizModalArrayList.add(new Games("Which of the following is an example of a biodegradable material?", "Styrofoam", "Plastic bags", "Food scraps", "Aluminum foil", "Food scraps"));
        quizModalArrayList.add(new Games("What is the term used for recycling electronic waste?", "E-cycling", "E-waste management", "E-recycling", "Tech recycling", "E-cycling"));
        quizModalArrayList.add(new Games("What percentage of plastic waste is typically recycled globally?", "10%", "25%", "50%", "90%", "10%"));
        quizModalArrayList.add(new Games("Which of the following is the most effective way to reduce waste?", "Recycling paper", "Reducing consumption of single-use plastics", "Composting food scraps", "Burning trash", "Reducing consumption of single-use plastics"));
        quizModalArrayList.add(new Games("Which of the following materials can be recycled indefinitely without losing quality?", "Paper", "Plastic", "Aluminum", "Glass", "Aluminum"));
        quizModalArrayList.add(new Games("What is 'single-stream recycling'?", "A method where recyclables are sorted at home", "A process where all recyclables are placed together in one bin", "A recycling program for only plastics", "A type of recycling that requires pre-washing all materials", "A process where all recyclables are placed together in one bin"));
        quizModalArrayList.add(new Games("Which type of plastic is commonly recycled into clothing and accessories?", "Polypropylene (PP)", "Polystyrene (PS)", "Polyethylene Terephthalate (PET)", "Low-Density Polyethylene (LDPE)", "Polyethylene Terephthalate (PET)"));
        quizModalArrayList.add(new Games("Which item is typically made from recycled materials in the paper recycling process?", "Toilet paper", "Egg cartons", "Plastic containers", "Glass jars", "Egg cartons"));
        quizModalArrayList.add(new Games("Which of the following is an example of 'upcycling'?", "Turning old clothes into new clothing", "Melting down old plastic to make new plastic products", "Recycling paper into new paper products", "Burning paper waste for energy", "Turning old clothes into new clothing"));
        quizModalArrayList.add(new Games("What is the primary challenge of recycling plastic?", "High cost of processing", "Lack of plastic waste", "Plastic decomposes too quickly", "Difficulty in separating different types of plastic", "Difficulty in separating different types of plastic"));
        quizModalArrayList.add(new Games("What is the environmental impact of 'downcycling'?", "It reduces pollution by turning waste into more valuable products", "It reduces the quality of materials as they are recycled into products of lesser value", "It increases the demand for new raw materials", "It makes recycling more expensive", "It reduces the quality of materials as they are recycled into products of lesser value"));
        quizModalArrayList.add(new Games("Which of the following is an example of a product made from recycled ocean plastic?", "Water bottles", "Sneakers", "Aluminum foil", "Cardboard boxes", "Sneakers"));
        quizModalArrayList.add(new Games("What is the process of turning old tires into useful products called?", "Upcycling", "Shredding", "Tire recycling", "Composting", "Tire recycling"));
        quizModalArrayList.add(new Games("What is the primary environmental benefit of composting organic waste?", "It reduces the amount of plastic in landfills", "It helps prevent deforestation", "It generates energy from food scraps", "It reduces methane emissions and creates nutrient-rich soil", "It reduces methane emissions and creates nutrient-rich soil"));
        quizModalArrayList.add(new Games("What does 'biodegradable' mean in relation to waste materials?", "The material can be recycled into something else", "The material can break down naturally by microorganisms over time", "The material is processed into reusable products", "The material can be burned without releasing toxins", "The material can break down naturally by microorganisms over time"));
        quizModalArrayList.add(new Games("Why is it important to remove caps from plastic bottles before recycling?", "Caps are usually made from a different type of plastic that cannot be recycled with the bottle", "Caps can cause contamination and damage sorting equipment", "Caps are too small to be effectively processed", "All of the above", "All of the above"));
        quizModalArrayList.add(new Games("What is the term for using waste materials to create new products?", "Upcycling", "Downcycling", "Reusing", "Recycling", "Recycling"));
        quizModalArrayList.add(new Games("Which of the following is NOT a common type of plastic found in recyclable products?", "Polyethylene Terephthalate (PET)", "High-Density Polyethylene (HDPE)", "Polylactic Acid (PLA)", "Polyvinyl Chloride (PVC)", "Polylactic Acid (PLA)"));
        quizModalArrayList.add(new Games("What does the term 'compostable' mean for a material?", "It can be recycled into new products", "It can break down into organic matter in a composting environment", "It can be incinerated safely", "It can be reused without modification", "It can break down into organic matter in a composting environment"));
        quizModalArrayList.add(new Games("Which of the following is a challenge of recycling glass?", "It breaks into small pieces", "It is heavier and more expensive to transport", "It cannot be reused", "It contaminates paper products", "It is heavier and more expensive to transport"));
        quizModalArrayList.add(new Games("What type of paper is most commonly recycled?", "Wet paper towels", "Newspapers", "Pizza boxes", "Cardboard with plastic coating", "Newspapers"));
        quizModalArrayList.add(new Games("Which of the following items should be disposed of in a hazardous waste facility instead of recycling bins?", "Old batteries", "Plastic bottles", "Magazines", "Glass jars", "Old batteries"));
        quizModalArrayList.add(new Games("How can you reduce the amount of plastic waste in your daily life?", "By recycling plastic waste more often", "By avoiding single-use plastics", "By buying more plastic products", "By incinerating plastic waste", "By avoiding single-use plastics"));
        quizModalArrayList.add(new Games("Which of the following is considered a non-recyclable item?", "Aluminum cans", "Glass bottles", "Styrofoam packaging", "Cardboard boxes", "Styrofoam packaging"));

    }
}