package com.example.admins.snaphotel.Maps.distance_matrix;

import java.util.List;

/**
 * Created by vanph on 03/02/2018.
 */

public class DistanceResponse {
    public List<Rows> rows;
    public class Rows{
        public List<Elements> elements;
        public class Elements{
            public Distance distance;
            public String status;
            public class Distance{
                public String text;
                public int value;
            }
        }

    }

}
