package middleware;

import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

public class MockTargetDataLine implements TargetDataLine{
    private AudioFormat format;

    public MockTargetDataLine() {
        this.format = new AudioFormat(44100, 16, 1, true, true);
    }

    @Override
    public void drain() {
        // Do nothing
    }

    @Override
    public void flush() {
        // Do nothing
    }

    @Override
    public void start() {
        // Do nothing
    }

    @Override
    public void stop() {
        // Do nothing
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public AudioFormat getFormat() {
        return format;
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public int getFramePosition() {
        return 0;
    }

    @Override
    public long getLongFramePosition() {
        return 0;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public float getLevel() {
        return 0;
    }

    @Override
    public javax.sound.sampled.Line.Info getLineInfo() {
        return null;
    }

    @Override
    public void open() throws LineUnavailableException {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public Control[] getControls() {
        return null;
    }

    @Override
    public boolean isControlSupported(Type control) {
        return true;
    }

    @Override
    public Control getControl(Type control) {
        return null;
    }

    @Override
    public void addLineListener(LineListener listener) {
        // do nothing
    }

    @Override
    public void removeLineListener(LineListener listener) {
        // do nothing
    }

    @Override
    public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
        // do nothing
    }

    @Override
    public void open(AudioFormat format) throws LineUnavailableException {
        // do nothing
    }

    @Override
    public int read(byte[] b, int off, int len) {
        return 0;
    }

}