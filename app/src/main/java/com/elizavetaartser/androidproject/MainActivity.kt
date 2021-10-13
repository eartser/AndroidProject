package com.elizavetaartser.androidproject

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.elizavetaartser.androidproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

}