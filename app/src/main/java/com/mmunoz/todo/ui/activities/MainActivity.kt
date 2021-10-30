package com.mmunoz.todo.ui.activities

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.mmunoz.todo.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private val currentUser by lazy { FirebaseAuth.getInstance().currentUser }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = if (currentUser == null) {
            R.id.LoginFragment
        } else {
            R.id.MyTaskFragment
        }
        navController.graph = navGraph

//
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        navController.graph.startDestination = if (currentUser == null) {
//            R.id.LoginFragment
//        } else {
//            R.id.MyTaskFragment
//        }
    }

    override fun androidInjector() = androidInjector
}