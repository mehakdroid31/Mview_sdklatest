package com.functionapps.mview_sdk2.helper;

import java.util.ArrayList;
import java.util.HashMap;

public class NeighboringCellsInfo {
    public static HashMap<Integer, String> lte_neighborCells;

    public static ArrayList<HashMap<Integer, String>> neighboringCellList=new ArrayList<>();


    public static ArrayList<String> lteParams;
    public static HashMap<Integer, String> wcdma_neighborCells;

    public static HashMap<Integer, String> nr_neighborCells;
    public static ArrayList<HashMap<Integer, String>> wcdma_neighboringCellList;
    public static ArrayList<String> wcdmaParams;

    public static ArrayList<String> nrParams;
    public static ArrayList<HashMap<Integer, Integer>> gsm_neighboringCellList;

    public static ArrayList<HashMap<Integer, String>> nr_neighboringCellList;

    public static ArrayList<String> gsmParams;
    public static int lte_neighbor_ss;
    public static int threeG_neighbor_ss;
    public static int gsm_neighnor_ss;




}
