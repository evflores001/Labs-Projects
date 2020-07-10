// pc_mod.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module pc_mod (
   output reg [31:0] PC,             // Output of pc_mod
   input wire [31:0] npc             // Input of pc_mod
   );

   initial begin
      PC <= 0;
   end
   always @ ( npc) begin
      #1 PC <= npc;
   end

endmodule 
