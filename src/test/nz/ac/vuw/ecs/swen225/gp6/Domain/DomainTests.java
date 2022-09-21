package test.nz.ac.vuw.ecs.swen225.gp6.Domain;


import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;

import java.util.stream.IntStream;

import junit.*;

public class DomainTests {

	// public String toString() {
	// 	String r = "";
	// 	for(int row=8;row!=0;row--) {
	// 		r += row + "|";
	// 		for(int col=1;col<=8;col++) {
	// 			Piece p = pieces[row][col];
	// 			if(p != null) {
	// 				r += p + "|";
	// 			} else {
	// 				r += "_|";
	// 			}
	// 		}
	// 		r += "\n";
	// 	}
	// 	return r + "  a b c d e f g h";
	// }

    // public String toString(Maze m){
    //     Tile[][] tileArray = m.getTileArrayCopy();
    //     String r = "";
    //     IntStream.range(0, m.width()).forEach(
    //         x -> {
    //             IntStream.range(0, m.height()).forEach(
    //                 y -> {
    //                     Tile t = tileArray[x][y];
    //                     if(t != null){
    //                         r += t.type() + "|";
    //                     } else {
    //                         r += "_|";
    //                     }
    //                 }
    //             );
    //             r += "\n";
    //         }
    //     );
    //     );
    // }

}
