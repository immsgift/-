package com.example.data.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class NafahatResult(
    val ayahArabic: String,
    val surahAndAyah: String,
    val gentleAdvice: String,
    val recommendedDhikr: String
)

object GeminiNafahatService {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val MODEL_NAME = "gemini-3.5-flash"

    suspend fun consultSpiritualGuide(userFeeling: String): NafahatResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // If key is missing or blank, provide rich pre-composed contextual islamic guidance
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineSpiritualAdvice(userFeeling)
        }

        try {
            val systemPrompt = """
                أنت 'المستشار الإيماني للمواقف اليومية' (نفحات إيمانية) في تطبيق القرآن الكريم.
                المستخدم يصف حالته النفسية أو موقفه اليومي (مثل قلق، حزن، توتر امتحانات، غضب، فرح، ابتلاء).
                مهمتك بدقة:
                1. اختيار آية قرآنية كريمة مناسبة تماماً لما يشعر به تلامس قلبه وتهدئ روعه.
                2. اسم السورة ورقم الآية بدقة (مثال: سورة الشرح: الآية ٥-٦).
                3. نصيحة روحانية وتدبر موجز مفعم بالرحمة والأمل (سرد لطيف في فقرة قصيرة لا تتجاوز 4 أسطر).
                4. ذكر أو دعاء نبوي قصير ومؤثر يكرره الآن.
                
                يجب أن تكون إجابتك بصيغة JSON فقط بهذا التنسيق وبدون أي نصوص إضافية أو علامات markdown codeblock:
                {
                  "ayahArabic": "نص الآية القرآنية مضبوطاً بالشكل إن أمكن",
                  "surahAndAyah": "اسم السورة: رقم الآية",
                  "gentleAdvice": "النصيحة الروحية اللطيفة المريحة للقلب",
                  "recommendedDhikr": "الذكر أو الدعاء المقترح"
                }
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "المستخدم يقول: $userFeeling")
                            })
                        })
                    })
                }
                put("contents", contents)

                val sysInst = JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                }
                put("systemInstruction", sysInst)

                val genConfig = JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                }
                put("generationConfig", genConfig)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = requestJson.toString().toRequestBody(mediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val respString = response.body?.string() ?: ""
                val root = JSONObject(respString)
                val candidates = root.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text", "")
                        val cleanedText = text.replace("```json", "").replace("```", "").trim()
                        val resultJson = JSONObject(cleanedText)
                        return@withContext NafahatResult(
                            ayahArabic = resultJson.optString("ayahArabic", "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾"),
                            surahAndAyah = resultJson.optString("surahAndAyah", "سورة الرعد: ٢٨"),
                            gentleAdvice = resultJson.optString("gentleAdvice", "اطمئن، فكل أمرك بيد أرحم الراحمين."),
                            recommendedDhikr = resultJson.optString("recommendedDhikr", "لا حول ولا قوة إلا بالله العلي العظيم")
                        )
                    }
                }
            }
        } catch (_: Exception) {
            // Graceful fallback to offline heuristic
        }

        getOfflineSpiritualAdvice(userFeeling)
    }

    private fun getOfflineSpiritualAdvice(feeling: String): NafahatResult {
        val f = feeling.lowercase()
        return when {
            f.contains("امتحان") || f.contains("اختبار") || f.contains("دراسة") || f.contains("قلق") || f.contains("توتر") -> {
                NafahatResult(
                    ayahArabic = "﴿ وَقُل رَّبِّ أَدْخِلْنِي مُدْخَلَ صِدْقٍ وَأَخْرِجْنِي مُخْرَجَ صِدْقٍ وَاجْعَل لِّي مِن لَّدُنكَ سُلْطَانًا نَّصِيرًا ﴾",
                    surahAndAyah = "سورة الإسراء: ٨٠",
                    gentleAdvice = "القلق شعور طبيعي ينم عن حرصك، لكن اعلم أن التوفيق بيد الله وحده وما عليك إلا بذل الوسع. استعن بالله ولا تعجز وتوكل عليه بيقين، فالأمور كلها مقدورة بخير لك.",
                    recommendedDhikr = "اللهم لا سهل إلا ما جعلته سهلاً، وأنت تجعل الحزن إذا شئت سهلاً."
                )
            }
            f.contains("حزن") || f.contains("ضيق") || f.contains("هم") || f.contains("غم") || f.contains("اكتئاب") -> {
                NafahatResult(
                    ayahArabic = "﴿ لَا تَحْزَنْ إِنَّ اللَّهَ مَعَنَا ﴾",
                    surahAndAyah = "سورة التوبة: ٤٠",
                    gentleAdvice = "مهما أظلمت الدنيا في عينيك، فإن معية الله أقرب إليك من حبل الوريد. سيعقب هذا الضيق فرجٌ عظيم ينسيك ما مضى، فالله لا ينسى قلباً لجأ إليه.",
                    recommendedDhikr = "لا إله إلا أنت سبحانك إني كنت من الظالمين."
                )
            }
            f.contains("غضب") || f.contains("ظلم") || f.contains("خلاف") || f.contains("مشكلة") -> {
                NafahatResult(
                    ayahArabic = "﴿ وَالْكَاظِمِينَ الْغَيْظَ وَالْعَافِينَ عَنِ النَّاسِ ۗ وَاللَّهُ يُحِبُّ الْمُحْسِنِينَ ﴾",
                    surahAndAyah = "سورة آل عمران: ١٣٤",
                    gentleAdvice = "الغضب جمرة من الشيطان تطفئها الاستعاذة والوضوء والسكوت. العفو ليس ضعفاً بل هو قمة الشجاعة وعزة النفس التي يرفعك الله بها درجات في الدنيا والآخرة.",
                    recommendedDhikr = "أعوذ بالله من الشيطان الرجيم، واستغفر الله العظيم."
                )
            }
            f.contains("مرض") || f.contains("تعب") || f.contains("وجع") || f.contains("ألم") -> {
                NafahatResult(
                    ayahArabic = "﴿ وَإِذَا مَرِضْتُ فَهُوَ يَشْفِينِ ﴾",
                    surahAndAyah = "سورة الشعراء: ٨٠",
                    gentleAdvice = "كل ألم ووعكة تصيب المؤمن هي رفعة لدرجاته وتكفير لسيئاته. الشفاء بيده سبحانه فاطمئن ولا تجزع واسأل الله العافية التامة.",
                    recommendedDhikr = "اللهم رب الناس، أذهب الباس، واشف أنت الشافي لا شفاء إلا شفاؤك."
                )
            }
            f.contains("رزق") || f.contains("فقر") || f.contains("دين") || f.contains("مال") -> {
                NafahatResult(
                    ayahArabic = "﴿ وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا * وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ﴾",
                    surahAndAyah = "سورة الطلاق: ٢-٣",
                    gentleAdvice = "خزائن السماوات والأرض بيد الرزاق ذي القوة المتين. اجعل تقوى الله وحسن التوكل والعمل بالأسباب طريقك، والفرج أقرب مما تظن.",
                    recommendedDhikr = "حسبنا الله ونعم الوكيل، وأفوض أمري إلى الله إن الله بصير بالعباد."
                )
            }
            else -> {
                NafahatResult(
                    ayahArabic = "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                    surahAndAyah = "سورة الرعد: ٢٨",
                    gentleAdvice = "مهما كانت تقلبات يومك ومواقف حياتك، فإن سكينة النفس الحقيقية تكمن في صلتك بربك وحسن ظنك به. تنفس بعمق وتوكل على الحي القيوم.",
                    recommendedDhikr = "يا حي يا قيوم برحمتك أستغيث، أصلح لي شأني كله ولا تكلني إلى نفسي طرفة عين."
                )
            }
        }
    }
}
