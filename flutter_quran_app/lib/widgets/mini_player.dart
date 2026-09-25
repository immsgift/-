import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../services/audio_player_service.dart';
import '../theme/app_theme.dart';

class AudioMiniPlayer extends StatelessWidget {
  const AudioMiniPlayer({super.key});

  @override
  Widget build(BuildContext context) {
    final audio = context.watch<QuranAudioPlayerService>();

    if (!audio.isPlaying && !audio.isBuffering && audio.currentTitle.isEmpty) {
      return const SizedBox.shrink();
    }

    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      decoration: BoxDecoration(
        color: AppColors.emeraldDark,
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.2),
            blurRadius: 8,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Row(
            children: [
              Container(
                width: 40,
                height: 40,
                decoration: BoxDecoration(
                  color: AppColors.goldAccent.withOpacity(0.2),
                  shape: BoxShape.circle,
                ),
                child: audio.isBuffering
                    ? const Padding(
                        padding: EdgeInsets.all(10),
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: AppColors.goldAccent,
                        ),
                      )
                    : const Icon(
                        Icons.volume_up_rounded,
                        color: AppColors.goldAccent,
                      ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      audio.currentTitle.isNotEmpty ? audio.currentTitle : 'القرآن الكريم',
                      style: const TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        fontSize: 14,
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    Text(
                      audio.currentSubtitle.isNotEmpty ? audio.currentSubtitle : 'جارٍ الاستماع...',
                      style: TextStyle(
                        color: Colors.white.withOpacity(0.7),
                        fontSize: 11,
                      ),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                ),
              ),
              IconButton(
                icon: Icon(
                  audio.isPlaying ? Icons.pause_circle_filled : Icons.play_circle_filled,
                  color: AppColors.goldAccent,
                  size: 34,
                ),
                onPressed: () {
                  if (audio.isPlaying) {
                    audio.pause();
                  } else {
                    audio.resume();
                  }
                },
              ),
              IconButton(
                icon: const Icon(Icons.close, color: Colors.white70, size: 20),
                onPressed: () => audio.stop(),
              ),
            ],
          ),
          if (audio.duration.inMilliseconds > 0)
            LinearProgressIndicator(
              value: (audio.position.inMilliseconds / audio.duration.inMilliseconds).clamp(0.0, 1.0),
              backgroundColor: Colors.white24,
              color: AppColors.goldAccent,
              minHeight: 2,
            ),
        ],
      ),
    );
  }
}
