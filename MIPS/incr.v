// incr.v
// Daniel Ortega [005326473] , Ben

module incrementer (
   input  wire [31:0] pcin,      // Input of incrementer
   output wire [31:0] pcout      // Output of incrementer
   );
   assign pcout = pcin + 1;  
endmodule 
