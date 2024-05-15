set ns [new Simulator] # Object of simulator class

# Define different colors for data flows (for NAM)
$ns color 1 Blue
$ns color 2 Red

# Open the Trace files
set file1 [open out.tr w]
$ns trace-all $file1

# Open the NAM trace file1
set file2 [open out.nam w]
$ns namtrace-all $file2

# Define a 'finish' procedure
proc finish {} {
    global ns file1 file2
    $ns flush-trace
    close $file1
    close $file2
    exit 0
}

# Create 6 nodes
set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]

# Create links between the nodes
# DropTail - first in first out queue
$ns duplex-link $n0 $n2 2Mb 10ms DropTail
$ns duplex-link $n1 $n2 2Mb 10ms DropTail
$ns simplex-link $n2 $n3 0.3Mb 100ms DropTail
$ns simplex-link $n3 $n2 0.3Mb 100ms DropTail
$ns duplex-link $n3 $n4 0.5Mb 40ms DropTail
$ns duplex-link $n3 $n5 0.5Mb 30ms DropTail

# Give node position (for NAM)
# with respect to $n0, $n2 is right down
$ns duplex-link-op $n0 $n2 orient right-down
$ns duplex-link-op $n1 $n2 orient right-up
$ns simplex-link-op $n2 $n3 orient right
$ns simplex-link-op $n3 $n2 orient left
$ns duplex-link-op $n3 $n4 orient right-up
$ns duplex-link-op $n3 $n5 orient right-down

# Set Queue Size of link (n2-n3) to 10
$ns queue-limit $n2 $n3 10

# Set a TCP connection
set tcp [new Agent/TCP]
$ns attach-agent $n0 $tcp 
set sink [new Agent/TCPSink]
$ns attach-agent $n4 $sink
$ns connect $tcp $sink
# fid is flow id (setting the color 1 - Blue)
$tcp set fid_ 1
$tcp set packetSize_ 552

# Setup a FTP over the TCP connection (ftp - application layer, TCP - transport layer)
set ftp [new Application/FTP]
$ftp attach-agent $tcp

# Setup a UDP connection
set udp [new Agent/UDP]
$ns attach-agent $n1 $udp
set null [new Agent/Null]
$ns attach-agent $n5 $null
$ns connect $udp $null
$udp set fid_ 2

# Setup a CBR over UDP connection (cbr - application layer, constant bit rate)
set cbr [new Application/Traffic/CBR]
$cbr attach-agent $udp
$cbr set packet_size_ 1000

$ns at 0.1 "$cbr start"
$ns at 1.0 "$ftp start"
$ns at 624.0 "$ftp stop"
$ns at 624.5 "$cbr stop"

$ns at 625.0 "finish"
$ns run