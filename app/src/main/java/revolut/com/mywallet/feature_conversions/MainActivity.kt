package revolut.com.mywallet.feature_conversions

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerAppCompatActivity
import revolut.com.mywallet.R
import revolut.com.mywallet.databinding.ActivityMainBinding
import revolut.com.mywallet.utils.hideKeyboard
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let { viewModel.selectedConversion = it.getParcelable(KEY_CONVERSION) }

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).let {
            binding = it
            it.viewModel = viewModel
            it.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_DRAGGING && dy > 0) {
                        recyclerView.hideKeyboard()
                    }
                }
            })
            it.executePendingBindings()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CONVERSION, viewModel.selectedConversion)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        viewModel.onStop()
        binding.root.hideKeyboard()
        super.onStop()
    }

    companion object {
        private const val KEY_CONVERSION = "conversion"
    }
}
