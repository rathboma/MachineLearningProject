#! /usr/bin/env ruby

require 'rubygems'

File.open('data/ns.csv') do |file|
  while (line = file.gets) do
    d, other = line.strip.split(',')
    `wget -O data/images/#{d}.jpg -nc http://www.newscientist.com/data/images/ns/covers/#{d.gsub("-", "")}.jpg`
  end
end