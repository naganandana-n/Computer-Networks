# NS2 SIMULATION DIAGRAM - SELF PRACTICE Q.png

# RUNNING:
# filename.tcl
# nam filename.nam

set ns [new Simulator]

# set colors as given in question
$ns color 1 Blue
$ns color 2 Red

set file1 [open out.tr w]
$ns trace-all $file1

set file2 [open out.nam w]
$ns namtrace-all $file2

proc finish {} {
    global ns file1 file2
    $ns flush-trace
    close $file1
    close $file2
    exit 0
}

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]
set n6 [$ns node]
set n7 [$ns node]
set n8 [$ns node]
set n9 [$ns node]

$ns duplex-link $n0 $n1 2Mb 10ms DropTail
$ns duplex-link $n1 $n4 2Mb 10ms DropTail
$ns duplex-link $n2 $n3 2Mb 10ms DropTail
$ns duplex-link $n3 $n4 2Mb 10ms DropTail
$ns duplex-link $n4 $n5 0.3Mb 100ms DropTail
$ns duplex-link $n5 $n6 0.5Mb 40ms DropTail
$ns duplex-link $n5 $n7 0.5Mb 30ms DropTail
$ns duplex-link $n6 $n8 0.5Mb 30ms DropTail
$ns duplex-link $n7 $n9 0.5Mb 30ms DropTail

$ns duplex-link-op $n0 $n1 orient right
$ns duplex-link-op $n1 $n4 orient right-down
$ns duplex-link-op $n2 $n3 orient right
$ns duplex-link-op $n3 $n4 orient right-up
$ns duplex-link-op $n4 $n5 orient right
$ns duplex-link-op $n5 $n6 orient right-up
$ns duplex-link-op $n5 $n7 orient right-down
$ns duplex-link-op $n6 $n8 orient right
$ns duplex-link-op $n7 $n9 orient right

# set queue limit as given in question
$ns queue-limit $n4 $n5 10

set tcp [new Agent/TCP]
$ns attach-agent $n0 $tcp
set sink [new Agent/TCPSink]
$ns attach-agent $n8 $sink
$ns connect $tcp $sink
# set requirements according to question
$tcp set fid_ 1
$tcp set packetSize_ 552

set ftp [new Application/FTP]
$ftp attach-agent $tcp

set udp [new Agent/UDP]
$ns attach-agent $n2 $udp
set null [new Agent/Null]
$ns attach-agent $n9 $null
$ns connect $udp $null
$udp set fid_ 2

set cbr [new Application/Traffic/CBR]
$cbr attach-agent $udp
# set requirement according to question
$cbr set packet_size_ 1000

#set time according to question
$ns at 0.1 "$cbr start"
$ns at 1.0 "$ftp start"
$ns at 624.0 "$ftp stop"
$ns at 624.5 "$cbr stop"

$ns at 625.0 "finish"
$ns run