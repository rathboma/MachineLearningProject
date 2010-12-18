#! /usr/bin/env ruby
require 'rubygems'
require 'fileutils'

all = ARGV[0]
training = ARGV[1]
test = ARGV[2]

system("rm -rf #{File.join(training, "*")}")
system("rm -rf #{File.join(training, "*")}")


files = Dir.glob(File.join(all, '*'))

system("cp #{File.join(all, '*')} #{training}")
files = Dir.glob(File.join(training, '*'))
(0..50).each do |i|
  file = files[rand(files.size)]
  files.delete(file)
  system("mv #{file} #{test}")
end