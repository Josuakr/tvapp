package com.example.schub

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.webView)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                injectNavigationJS()
            }
        }

        val url = intent.getStringExtra("url") ?: "https://www.tagesschau.de"
        webView.loadUrl(url)
    }

    private fun injectNavigationJS() {
        val js = """
        (function() {
            const container = document.querySelector('div.mx-auto.max-w-full');
            
            if (!container) return;
        
            const focusable = Array.from(container.querySelectorAll('a.tabbable')).filter(el => el.offsetParent !== null);
            if (focusable.length === 0) return;

            let current = 0;

            // Alle Elemente bekommen eine Transition für sanften Zoom
            focusable.forEach(el => {
                el.style.transition = "transform 0.3s ease";
            });

            function highlight(i) {
                focusable.forEach((el, idx) => {
                    el.style.transform = idx === i ? "scale(1.3)" : "scale(1)";
                    if (idx === i) {
                        el.scrollIntoView({behavior: 'smooth', block: 'center'});
                    }
                });
            }

            function distance(a, b, direction) {
                const ra = a.getBoundingClientRect();
                const rb = b.getBoundingClientRect();

                if (direction === 'ArrowRight' && rb.left > ra.left) {
                    return Math.hypot(rb.left - ra.right, rb.top - ra.top);
                }
                if (direction === 'ArrowLeft' && rb.left < ra.left) {
                    return Math.hypot(ra.left - rb.right, rb.top - ra.top);
                }
                if (direction === 'ArrowDown' && rb.top > ra.top) {
                    return Math.hypot(rb.left - ra.left, rb.top - ra.bottom);
                }
                if (direction === 'ArrowUp' && rb.top < ra.top) {
                    return Math.hypot(rb.left - ra.left, ra.top - rb.bottom);
                }
                return Infinity;
            }

            function move(direction) {
                const currentEl = focusable[current];
                let best = current;
                let bestDist = Infinity;
                for (let i = 0; i < focusable.length; i++) {
                    if (i === current) continue;
                    const d = distance(currentEl, focusable[i], direction);
                    if (d < bestDist) {
                        bestDist = d;
                        best = i;
                    }
                }
                if (best !== current) {
                    current = best;
                    highlight(current);
                }
            }

            highlight(current);

            document.addEventListener('keydown', e => {
                if (['ArrowRight','ArrowLeft','ArrowDown','ArrowUp'].includes(e.key)) {
                    e.preventDefault();
                    move(e.key);
                } else if (e.key === 'Enter') {
                    //const el = focusable[current];
                    //if (el.tagName === "INPUT") {
                    //    el.focus();
                    //} else {
                    //    el.click();
                    //}
                    focusable[current].click();
                }
            });
        })();
    """.trimIndent()

        webView.evaluateJavascript(js, null)
    }
}