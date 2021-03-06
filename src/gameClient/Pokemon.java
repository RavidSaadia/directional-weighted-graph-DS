package gameClient;

import com.google.gson.JsonObject;
import api.*;
import gameClient.util.Point3D;

public class Pokemon {
    
    private edge_data _edge;
    private double _value;
    private Point3D _pos;
    private int _type;
    
    public Pokemon(JsonObject json_pokemon) {
        JsonObject pokemon = json_pokemon.getAsJsonObject("Pokemon");
        _value = pokemon.get("value").getAsInt();
        _type = pokemon.get("type").getAsInt();
        String[] raw_pos = pokemon.get("pos").getAsString().split(",");
        
        _pos = new Point3D(Double.parseDouble(raw_pos[0]), Double.parseDouble(raw_pos[1]), Double.parseDouble(raw_pos[2]));
        
        geo_location pos = new GeoLocation(_pos.x(), _pos.y(), _pos.z());
        _edge = this.findEdge(pos);
        
    }

    
    /** 
     * using triangle inequality, the algorithm search with BFS for an edge u,v such that dist(u,pos) + dist(pos,v) = dist(u,v)
     * @param pos
     * @return edge_data
     */
    private edge_data findEdge(geo_location pos) {
        for (node_data v : Myclient.g.getV()) {
            int src = v.getKey();
            for (edge_data e : Myclient.g.getE(v.getKey())) {
                int dest = e.getDest();
                node_data dest_node = Myclient.g.getNode(dest);
                if (_type > 0 && src < dest || _type < 0 && src > dest) {
                    geo_location src_loc = v.getLocation();
                    geo_location dest_loc = dest_node.getLocation();
                    double line = src_loc.distance(dest_loc);
                    double line_through_pos = src_loc.distance(pos) + pos.distance(dest_loc);
                    if (line > line_through_pos - 0.0000001) {
                        return e;
                    }
                }
            }
        }
        return null;
    }

    
    
    /** 
     * get the pokemon's edge.
     * @return edge_data
     */
    public edge_data get_edge() {
        return _edge;
    }
    
    
    /** 
     * get the pokemon's value.
     * @return double
     */
    public double get_value() {
        return _value;
    }
    

    /**
     * get the pokemon's position. 
     * @return Point3D
     */
    public Point3D get_pos() {
        return _pos;
    }
    
    
    /** 
     * get the pokemon's edge direction. represnted by: 1 from low key to high, -1 from high to low.
     * @return int
     */
    public int get_type(){
        return _type;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj != null){
            Pokemon other = (Pokemon)obj;
            return _pos.equalsXY(other.get_pos()) && _value == other.get_value() && _type == other.get_type();
        }
        return false;
    }
    
    
}
