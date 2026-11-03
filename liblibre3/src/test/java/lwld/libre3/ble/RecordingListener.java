package lwld.libre3.ble;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lwld.libre3.Libre3SensorState;
import lwld.libre3.data.HistoricalPage;
import lwld.libre3.data.PatchStatus;
import lwld.libre3.data.RealtimeReading;

/**
 * Keeps the stages, errors and statuses the collector would show. Safe to read from the test while the worker and the
 * main thread write to it.
 *
 * @author Asbjørn Aarrestad - 2026.11
 */
class RecordingListener implements Libre3Ble.Listener {

    final List<String> stages = new CopyOnWriteArrayList<>();
    final List<String> errors = new CopyOnWriteArrayList<>();
    final List<PatchStatus> statuses = new CopyOnWriteArrayList<>();

    @Override
    public void onStage(String stage) {
        stages.add(stage);
    }

    @Override
    public void onPaired(Libre3SensorState state) {
    }

    @Override
    public void onReading(RealtimeReading reading) {
    }

    @Override
    public void onStatus(PatchStatus status) {
        statuses.add(status);
    }

    @Override
    public void onHistory(List<HistoricalPage.Entry> entries) {
    }

    @Override
    public void onError(String message) {
        errors.add(message);
    }
}
