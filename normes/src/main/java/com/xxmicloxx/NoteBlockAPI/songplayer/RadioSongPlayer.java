package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.*;
import com.xxmicloxx.NoteBlockAPI.model.playmode.ChannelMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoStereoMode;
import com.xxmicloxx.NoteBlockAPI.utils.NoteUtils;
import org.bukkit.entity.Player;

/**
 * SongPlayer playing to everyone added to it no matter where he is
 *
 */
public class RadioSongPlayer extends SongPlayer {
	
	//protected boolean stereo = true;
	
	public RadioSongPlayer(Song song) {
		super(song);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	public RadioSongPlayer(Song song, SoundCategory soundCategory) {
		super(song, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	private RadioSongPlayer(com.xxmicloxx.NoteBlockAPI.SongPlayer songPlayer) {
		super(songPlayer);
	}

	public RadioSongPlayer(Playlist playlist, SoundCategory soundCategory) {
		super(playlist, soundCategory);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	public RadioSongPlayer(Playlist playlist) {
		super(playlist);
		makeNewClone(com.xxmicloxx.NoteBlockAPI.RadioSongPlayer.class);
	}

	@Override
	public void playTick(Player player, int tick) {
		byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

		for (Layer layer : song.getLayerHashMap().values()) {
			Note note = layer.getNote(tick);
			if (note == null) {
				continue;
			}

			float volume = (layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity()) / 100_00_00_00F;
			float pitch = NoteUtils.getPitch(note);

			channelMode.play(player, player.getEyeLocation(), song, layer, note, soundCategory, volume, pitch);
		}
	}

	/**
	 * Returns if the SongPlayer will play Notes from two sources as stereo
	 * @return if is played stereo
     * @deprecated
	 */
	public boolean isStereo(){
		return !(channelMode instanceof MonoMode);
	}
	
	/**
	 * Sets if the SongPlayer will play Notes from two sources as stereo
	 * @param stereo
     * @deprecated
	 */
	public void setStereo(boolean stereo){
		channelMode = stereo ? new MonoMode() : new MonoStereoMode();
	}

	public void setChannelMode(ChannelMode mode){
	    channelMode = mode;
    }
}
