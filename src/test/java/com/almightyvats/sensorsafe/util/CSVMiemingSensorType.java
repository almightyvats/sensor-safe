package com.almightyvats.sensorsafe.util;

import lombok.Getter;

public enum CSVMiemingSensorType {
    TIMESTAMP("Timestamp", 0),
    Tair_C("Tair_C", 1),
    RH_percent("RH_percent", 2),
    GlobRad_Wm_2("GlobRad_Wm-2", 3),
    SWC_1_5cm_m3m_3("SWC_1.5cm_m3m-3", 3),
    SWC_1_10cm_m3m_3("SWC_1.10cm_m3m-3", 4),
    SWC_1_20cm_m3m_3("SWC_1.20cm_m3m-3", 5),
    SWC_2_5cm_m3m_3("SWC_2.5cm_m3m-3", 6),
    SWC_2_10cm_m3m_3("SWC_2.10cm_m3m-3", 7),
    SWC_2_20cm_m3m_3("SWC_2.20cm_m3m-3", 8),
    Tsoil_1_5cm_C("Tsoil_1.5cm_C", 9),
    Tsoil_1_10cm_C("Tsoil_1.10cm_C", 10),
    Tsoil_1_20cm_C("Tsoil_1.20cm_C", 11),
    Tsoil_2_5cm_C("Tsoil_2.5cm_C", 12),
    Tsoil_2_10cm_C("Tsoil_2.10cm_C", 13),
    Tsoil_2_20cm_C("Tsoil_2.20cm_C", 14),
    Dendrometer_1_micro_m("Dendrometer_1_µm", 15),
    Dendrometer_2_micro_m("Dendrometer_2_µm", 16),
    Dendrometer_3_micro_m("Dendrometer_3_µm", 17),
    Dendrometer_4_micro_m("Dendrometer_4_µm", 18),
    Dendrometer_5_micro_m("Dendrometer_5_µm", 19),
    Dendrometer_6_micro_m("Dendrometer_6_µm", 20),
    Dendrometer_7_micro_m("Dendrometer_7_µm", 21);

    @Getter
    private final String name;

    @Getter
    private final int index;

    CSVMiemingSensorType(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
