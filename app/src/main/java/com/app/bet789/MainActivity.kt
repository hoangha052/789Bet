package com.app.bet789

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.bet789.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.btnSignIn.setOnClickListener {
            startActivity(InAppBrowserActivity.getIntent(this, "https://zingnews.vn"))
        }
        binding.btnSignUp.setOnClickListener {
            val phone = binding.etSdt.text.takeIf {
                it != null && it.trim().length >= 10
            }
            if (phone != null) {
                startActivity(InAppBrowserActivity.getIntent(this, "https://zingnews.vn"))
            } else {
                showAlert()
            }
        }
        binding.btnZalo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://zalo.me/lucas8686"))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this).create().apply {
            val title = SpannableString("Đăng Ký Không Thành Công")
            title.setSpan(ForegroundColorSpan(Color.parseColor("#ED4337")), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setTitle(title)
            setMessage("Vui lòng nhập đủ và đúng thông tin")
            setButton(DialogInterface.BUTTON_NEGATIVE, "Thoát") { _, _ -> cancel() }
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.info) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://zalo.me/lucas8686"))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}