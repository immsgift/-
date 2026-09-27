package com.example.ui.screens.ai

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight

data class FeelingCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val verses: List<SpiritualVerseItem>
)

data class SpiritualVerseItem(
    val ayahArabic: String,
    val surahAndAyah: String,
    val reflection: String,
    val recommendedDhikr: String
)

object SpiritualVersesDataSource {

    val categories = listOf(
        FeelingCategory(
            id = "tired",
            title = "تعبان ومُرهق",
            subtitle = "أشعر بالإرهاق، التعب الجسدي والضيق",
            icon = Icons.Default.SentimentDissatisfied,
            verses = listOf(
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَلَا تَيْأَسُوا مِن رَّوْحِ اللَّهِ ۖ إِنَّهُ لَا يَيْأَسُ مِن رَّوْحِ اللَّهِ إِلَّا الْقَوْمُ الْكَافِرُونَ ﴾",
                    surahAndAyah = "سورة يوسف: ٨٧",
                    reflection = "التعب والوهن يمرّ بهما كل إنسان، لكن رَوْح الله ورحمته ولطفه أوسع من كل إرهاق. استرح بالصلاة وفوّض أمرك لمن لا تأخذه سِنة ولا نوم.",
                    recommendedDhikr = "يا حي يا قيوم برحمتك أستغيث، أصلح لي شأني كله ولا تكلني إلى نفسي طرفة عين."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ﴾",
                    surahAndAyah = "سورة البقرة: ٢٨٦",
                    reflection = "اطمئن، فالله يعلم قدر طاقتك وضعفك ولن يحملك فوق ما تطيق؛ كل سجدة تخفف عنك، وكل تعب محتسب تكفير ورفعة.",
                    recommendedDhikr = "لا حول ولا قوة إلا بالله العلي العظيم."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَلَقَدْ نَعْلَمُ أَنَّكَ يَضِيقُ صَدْرُكَ بِمَا يَقُولُونَ ۝ فَسَبِّحْ بِحَمْدِ رَبِّكَ وَكُن مِّنَ السَّاجِدِينَ ﴾",
                    surahAndAyah = "سورة الحجر: ٩٧-٩٨",
                    reflection = "حين يثقل كاهلك التعب وضيق الصدر، فإن الدواء الرباني هو التسبيح والافتقار بالسجود، فالسجود يفرغ شحنات التعب ويبدل الوهن سكينة.",
                    recommendedDhikr = "سبحان الله وبحمده، سبحان الله العظيم."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَتَوَكَّلْ عَلَى الْحَيِّ الَّذِي لَا يَمُوتُ وَسَبِّحْ بِحَمْدِهِ ﴾",
                    surahAndAyah = "سورة الفرقان: ٥٨",
                    reflection = "البشر يضعفون ويعجزون، أما الله فهو الحي القيوم الدائم القوة والعطاء؛ ألقِ كل أتعابك ومخاوفك بين يديه وسيتولاك بلطفه.",
                    recommendedDhikr = "توكلت على الحي الذي لا يموت، والحمد لله الذي لم يتخذ ولداً."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَاصْبِرْ لِحُكْمِ رَبِّكَ فَإِنَّكَ بِأَعْيُنِنَا ۖ وَسَبِّحْ بِحَمْدِ رَبِّكَ حِينَ تَقُومُ ﴾",
                    surahAndAyah = "سورة الطور: ٤٨",
                    reflection = "ما أجمل وأعظم هذا الوعد الإلهي: 'فَإِنَّكَ بِأَعْيُنِنَا'! كل لحظة تعب وصبر أنت فيها تحت رعاية الله وعينه التي لا تنام.",
                    recommendedDhikr = "حسبي ربي من كل شيء، حسبي الله ونعم الوكيل."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ أَلَيْسَ اللَّهُ بِكَافٍ عَبْدَهُ ﴾",
                    surahAndAyah = "سورة الزمر: ٣٦",
                    reflection = "استفهام تقريري يملأ الروح طمأنينة؛ كفاية الله لك تغنيك عن كل أحد وتحميك من كل تعب وخوف ووهن.",
                    recommendedDhikr = "اللهم اكفني بحلالك عن حرامك، وبفضلك عمن سواك."
                )
            )
        ),
        FeelingCategory(
            id = "sad",
            title = "حزين ومهموم",
            subtitle = "قلبي متألم وضائق من هموم الدنيا",
            icon = Icons.Default.MoodBad,
            verses = listOf(
                SpiritualVerseItem(
                    ayahArabic = "﴿ لَا تَحْزَنْ إِنَّ اللَّهَ مَعَنَا ﴾",
                    surahAndAyah = "سورة التوبة: ٤٠",
                    reflection = "كلمة الصدق النبوي في أحلك لحظات الغار؛ معية الله كافية لتبديد كل حزن وظلمة وخوف. لن يضيع قلبٌ أيقن أن ربه معه.",
                    recommendedDhikr = "حسبنا الله ونعم الوكيل، نعم المولى ونعم النصير."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا ﴾",
                    surahAndAyah = "سورة الشرح: ٥-٦",
                    reflection = "وعد إلهي مكرر ومؤكد، لن يغلب عسرٌ يسرين. اليُسر يولد مع قلب الشدة ذاتها، وسيبعث الله بعد هذا الحزن فرحاً يتعجب منه قلبك.",
                    recommendedDhikr = "لا إله إلا أنت سبحانك إني كنت من الظالمين."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَبَشِّرِ الصَّابِرِينَ ۝ الَّذِينَ إِذَا أَصَابَتْهُم مُّصِيبَةٌ قَالُوا إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ ﴾",
                    surahAndAyah = "سورة البقرة: ١٥٥-١٥٦",
                    reflection = "البشارة العظمى من الخالق للصابرين؛ كل ألم تخفيه ودمعة تحبسها مسجلة عنده سبحانه وسيعوضك خيراً مما فاتك.",
                    recommendedDhikr = "إنا لله وإنا إليه راجعون، اللهم أجرني في مصيبتي واخلف لي خيراً منها."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ ﴾",
                    surahAndAyah = "سورة البقرة: ١٨٦",
                    reflection = "أقرب مما تظن! ليس بينك وبينه حجاب؛ في أوج حزنك وخلوتك ارفع يديك وبث شكواك لسميع الدعاء.",
                    recommendedDhikr = "يا فارج الهم ويا كاشف الغم، فرج همي ويسر أمري وارحم ضعفي."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                    surahAndAyah = "سورة الرعد: ٢٨",
                    reflection = "القلب المضطرب الحزين لا يسكنه جاه ولا مال، إنما سكناه وسكونه في ذكر مولاه والاتصال بحبله المتين.",
                    recommendedDhikr = "لا إله إلا الله وحده لا شريك له، له الملك وله الحمد وهو على كل شيء قدير."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَأُفَوِّضُ أَمْرِي إِلَى اللَّهِ ۚ إِنَّ اللَّهَ بَصِيرٌ بِالْعِبَادِ ﴾",
                    surahAndAyah = "سورة غافر: ٤٤",
                    reflection = "التفويض راحة النفس؛ حين تعجز حيلتك سلّم زمام أمرك لمدبر الأكوان العليم بحالك وبصير بضعفك.",
                    recommendedDhikr = "أفوض أمري إلى الله، إن الله بصير بالعباد."
                )
            )
        ),
        FeelingCategory(
            id = "happy",
            title = "فرحان ومستبشر",
            subtitle = "أشعر بالسعادة والشكر والامتنان لله",
            icon = Icons.Default.Celebration,
            verses = listOf(
                SpiritualVerseItem(
                    ayahArabic = "﴿ قُلْ بِفَضْلِ اللَّهِ وَبِرَحْمَتِهِ فَبِذَٰلِكَ فَلْيَفْرَحُوا هُوَ خَيْرٌ مِّمَّا يَجْمَعُونَ ﴾",
                    surahAndAyah = "سورة يونس: ٥٨",
                    reflection = "أعظم الفرح هو الفرح بهداية الله ورضوانه ولطفه بك وبأهلك. قيّد فرحتك بدوام الحمد والشكر حتى تدوم وتزداد بركتها.",
                    recommendedDhikr = "الحمد لله حمداً كثيراً طيباً مباركاً فيه."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ﴾",
                    surahAndAyah = "سورة إبراهيم: ٧",
                    reflection = "الشكر هو حارس النعم ومستجلب المزيد منها؛ ما استديمت نعم الله بمثل شكرها والتواضع له ونفع عباده بها.",
                    recommendedDhikr = "اللهم ما أصبح بي من نعمة أو بأحد من خلقك فمنك وحدك لا شريك لك، فلك الحمد ولك الشكر."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَآتَاكُم مِّن كُلِّ مَا سَأَلْتُمُوهُ ۚ وَإِن تَعُدُّوا نِعْمَتَ اللَّهِ لَا تُحْصُوهَا ﴾",
                    surahAndAyah = "سورة إبراهيم: ٣٤",
                    reflection = "تأمل نعم الله التي تحيط بك من كل جانب، واجعل فرحتك سبباً في إدخال السرور على قلوب المحتاجين والضعفاء.",
                    recommendedDhikr = "اللهم أعني على ذكرك وشكرك وحسن عبادتك."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَأَمَّا بِنِعْمَةِ رَبِّكَ فَحَدِّثْ ﴾",
                    surahAndAyah = "سورة الضحى: ١١",
                    reflection = "التحدث بنعم الله اعترافاً بفضله وإحسانه يورث القلب تواضعاً ومحبة للخالق المنعم.",
                    recommendedDhikr = "الحمد لله الذي بنعمته تتم الصالحات."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ إِنَّ الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ يَهْدِيهِمْ رَبُّهُم بِإِيمَانِهِمْ ۖ تَجْرِي مِن تَحْتِهِمُ الْأَنْهَارُ فِي جَنَّاتِ النَّعِيمِ ﴾",
                    surahAndAyah = "سورة يونس: ٩",
                    reflection = "السعادة الحقيقية تبدأ بنور الإيمان في الدنيا وتكتمل بالنعيم المقيم في جنات الخلد.",
                    recommendedDhikr = "رضيت بالله رباً وبالإسلام ديناً وبمحمد ﷺ نبياً ورسولاً."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ هَلْ جَزَاءُ الْإِحْسَانِ إِلَّا الْإِحْسَانُ ﴾",
                    surahAndAyah = "سورة الرحمن: ٦٠",
                    reflection = "سنة الله الجارية؛ من أحسن النية والعمل وأقبل على ربه، غمره الله بإحسانه وأفاض عليه من كرمه وفرحه.",
                    recommendedDhikr = "سبحان الله والحمد لله ولا إله إلا الله والله أكبر."
                )
            )
        ),
        FeelingCategory(
            id = "loving",
            title = "مُحب ومُشتاق",
            subtitle = "يفيض قلبي بمحبة الله ورسوله والخير للناس",
            icon = Icons.Default.Favorite,
            verses = listOf(
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَالَّذِينَ آمَنُوا أَشَدُّ حُبًّا لِّلَّهِ ﴾",
                    surahAndAyah = "سورة البقرة: ١٦٥",
                    reflection = "محبة الله هي أزكى المشاعر وأعلاها، بها تحلو الطاعات وتهون التضحيات. من أحب ربه أنس بذكره واطمأن بقربه.",
                    recommendedDhikr = "اللهم إني أسألك حبك، وحب من يحبك، وحب عمل يقربني إلى حبك."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ قُلْ إِن كُنتُمْ تُحِبُّونَ اللَّهَ فَاتَّبِعُونِي يُحْبِبْكُمُ اللَّهُ وَيَغْفِرْ لَكُمْ ذُنُوبَكُمْ ﴾",
                    surahAndAyah = "سورة آل عمران: ٣١",
                    reflection = "صدق المحبة يظهر في اتباع هدي الحبيب المصطفى ﷺ في أخلاقه ورحمته وتواضعه مع كل الناس.",
                    recommendedDhikr = "اللهم صل وسلم وبارك على نبينا وحبيبنا محمد وعلى آله وصحبه أجمعين."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ إِنَّ الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ سَيَجْعَلُ لَهُمُ الرَّحْمَٰنُ وُدًّا ﴾",
                    surahAndAyah = "سورة مريم: ٩٦",
                    reflection = "إذا أحب الله عبداً وضع له المحبة والقبول والود الصادق في قلوب أهل الأرض والسماء.",
                    recommendedDhikr = "سبحان الله وبحمده عدد خلقه ورضا نفسه وزنة عرشه ومداد كلماته."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ رَّحْمَٰنِ رَّحِيمٍ ۝ يُحِبُّهُمْ وَيُحِبُّونَهُ ﴾",
                    surahAndAyah = "سورة المائدة: ٥٤",
                    reflection = "شرف ليس بعده شرف؛ أن يحبك رب العزة والجلال، فاجعل رضا ومحبة الله غايتك الأسمى في كل عمل.",
                    recommendedDhikr = "يا ودود يا ودود، يا ذا العرش المجيد، يا فعالاً لما تريد."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَاخْفِضْ جَنَاحَكَ لِلْمُؤْمِنِينَ ﴾",
                    surahAndAyah = "سورة الحجر: ٨٨",
                    reflection = "المحبة الإيمانية تتجسد في لين الجانب، والتواضع لإخوانك، وصفاء السريرة من الغل والحسد.",
                    recommendedDhikr = "اللهم ألف بين قلوبنا وأصلح ذات بيننا واهدنا سبل السلام."
                ),
                SpiritualVerseItem(
                    ayahArabic = "﴿ وَتَوَاصَوْا بِالصَّبْرِ وَتَوَاصَوْا بِالْمَرْحَمَةِ ﴾",
                    surahAndAyah = "سورة البلد: ١٧",
                    reflection = "الرحمة هي تاج المحبة؛ الراحمون يرحمهم الرحمن، ارحم من في الأرض يرحمك من في السماء.",
                    recommendedDhikr = "اللهم ارحمنا وارحم والدينا والمسلمين أجمعين."
                )
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiNafahatScreen(
    modifier: Modifier = Modifier
) {
    val categories = SpiritualVersesDataSource.categories
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var currentVerseIndex by remember { mutableIntStateOf(0) }

    val currentCategory = categories[selectedCategoryIndex]
    val totalVersesInCat = currentCategory.verses.size
    val currentVerse = currentCategory.verses[currentVerseIndex % totalVersesInCat]

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("ai_nafahat_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "آية لقلبك حسب حالتك (نفحات إيمانية)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldDark,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Intro Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(EmeraldDark, EmeraldPrimary)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(GoldAccent.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = GoldLight,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "اختر حالتك القلبية الآن",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 17.sp
                                    )
                                    Text(
                                        text = "مجموعة واسعة من آيات الطمأنينة والشرح المعتمد لكل حالة",
                                        color = GoldLight,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Feeling Selection Tabs
            item {
                Text(
                    text = "بماذا تشعر في هذه اللحظة؟",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEachIndexed { index, cat ->
                        val isSelected = selectedCategoryIndex == index
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    if (selectedCategoryIndex != index) {
                                        selectedCategoryIndex = index
                                        currentVerseIndex = 0
                                    }
                                }
                                .testTag("feeling_tab_${cat.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surface
                            ),
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.title,
                                    tint = if (isSelected) GoldLight else EmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = cat.title.split(" ").firstOrNull() ?: cat.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Action Button: "أعطني آية أخرى لهذه الحالة" with counter
            item {
                Button(
                    onClick = {
                        currentVerseIndex = (currentVerseIndex + 1) % totalVersesInCat
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("get_another_ayah_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = EmeraldDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "آية أخرى لـ ${currentCategory.title} (${(currentVerseIndex % totalVersesInCat) + 1} من $totalVersesInCat)",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = EmeraldDark
                    )
                }
            }

            // Animated Verse Result Card
            item {
                AnimatedContent(
                    targetState = currentVerse,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "verse_transition"
                ) { verse ->
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        // Quran Ayah Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = GoldLight.copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent)
                        ) {
                            Column(
                                modifier = Modifier.padding(22.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "بلسم القرآن لقلبك",
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = verse.ayahArabic,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 34.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = EmeraldPrimary
                                ) {
                                    Text(
                                        text = verse.surahAndAyah,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Reflection
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = GoldAccent
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "شرح وتدبّر الآية:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = EmeraldPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = verse.reflection,
                                    fontSize = 15.sp,
                                    lineHeight = 24.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Recommended Dhikr
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = EmeraldContainer.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.VolunteerActivism,
                                        contentDescription = null,
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "دعاء وذكر مقترح لترديده الآن:",
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = verse.recommendedDhikr,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 26.sp,
                                    color = EmeraldDark
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
