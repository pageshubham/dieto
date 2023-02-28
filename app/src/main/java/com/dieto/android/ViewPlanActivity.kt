package com.dieto.android

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_view_plan.*

class ViewPlanActivity : AppCompatActivity() {

    private var mPlan = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_plan)

        mPlan = intent.getStringExtra("plan").toString()

        when (mPlan) {
            "vata" -> {
                view_plan_desc_text.text = "Note: Drink 1 glasses of warm water after wakeup if your tummy is out, Eat salad mix of carrot, cucumber, beetroot, radish, tomato etc & drink buttermilk after lunch."
                view_plan_image_view.setImageResource(R.drawable.diet_vata_plan)
            }
            "pitta" -> {
                view_plan_desc_text.text = "Note: Drink 2 glasses of warm water after wakeup if your tummy is out, Eat salad mix of carrot, cucumber, beetroot, radish, tomato etc & drink buttermilk after lunch."
                view_plan_image_view.setImageResource(R.drawable.diet_pitta_plan)
            }
            "kapha" -> {
                view_plan_desc_text.text = "Note: Drink 3 glasses of warm water after wakeup if your tummy is out, Eat salad before lunch mix of carrot, cucumber, beetroot, radish, tomato etc."
                view_plan_image_view.setImageResource(R.drawable.diet_kapha_plan)
            }
            "gym" -> {
                view_plan_desc_text.text = "Note: Drink 1 glass(lean body), 2 glasses(medium body), 3 glasses(fat body) of warm water after wakeup if your tummy is out, Eat salad before lunch mix of carrot, cucumber, beetroot, radish, tomato etc."
                view_plan_image_view.setImageResource(R.drawable.diet_gym_plan)
            }
        }

        view_plan_back_icon.setOnClickListener {
            finish()
        }

    }//onCreate()

}