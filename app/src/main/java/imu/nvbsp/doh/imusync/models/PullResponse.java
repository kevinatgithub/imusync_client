package imu.nvbsp.doh.imusync.models;

import java.util.ArrayList;

public class PullResponse {
    private ArrayList<PullData> data;

    public ArrayList<PullData> getData() {
        return data;
    }

    public class PullData {
        private Donor left;
        private Donor right;

        public Donor getLeft() {
            return left;
        }

        public Donor getRight() {
            return right;
        }
    }
}
