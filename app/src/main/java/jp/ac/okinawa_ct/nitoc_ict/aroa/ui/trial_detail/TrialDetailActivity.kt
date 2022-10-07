package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.trial_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import jp.ac.okinawa_ct.nitoc_ict.aroa.R
import jp.ac.okinawa_ct.nitoc_ict.aroa.databinding.ActivityTrialDetailBinding

class TrialDetailActivity : AppCompatActivity() {
    companion object {
        val TAG = TrialDetailActivity::class.simpleName

        private const val TRIAL_ID = "TRIAL_ID"

        fun makeIntent(context: Context, trialId: String): Intent {
            return Intent(context, TrialDetailActivity::class.java)
                .putExtra(TRIAL_ID, trialId)
        }
    }

    private lateinit var _binding: ActivityTrialDetailBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityTrialDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trialId = intent.getStringExtra(TRIAL_ID)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_config_join_trial) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.navigation_config_join_trial, R.id.navigation_select_ble_device
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)

        binding.startTrialFab.setOnClickListener {
            // TODO トライアルの開始処理
            Snackbar.make(it, trialId ?: "", Snackbar.LENGTH_LONG).show()
        }
    }
}