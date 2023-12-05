fun main() {
    data class MapRange(val source: Long, val destination: Long, val range: Long)
    data class SeedMap(val name: String, val association: MutableList<MapRange>)
    
    fun getAssociation(map: SeedMap, value: Long): Long {
        val range = map.association.firstOrNull { (value >= it.source) && (value < (it.source + it.range)) }
        if (range == null) return value
        else return range.destination + (value - range.source)
    }
    
    fun getLocation(seedList: List<SeedMap>, seed: Long): Long {
        val soil = getAssociation(seedList.first { it.name == "seed-to-soil" }, seed)
        val fertilizer = getAssociation(seedList.first { it.name == "soil-to-fertilizer" }, soil)
        val water = getAssociation(seedList.first { it.name == "fertilizer-to-water" }, fertilizer)
        val light = getAssociation(seedList.first { it.name == "water-to-light" }, water)
        val temperature = getAssociation(seedList.first { it.name == "light-to-temperature" }, light)
        val humidity = getAssociation(seedList.first { it.name == "temperature-to-humidity" }, temperature)
        val location = getAssociation(seedList.first { it.name == "humidity-to-location" }, humidity)
        return location!!
    }
    
    fun getMaps(input: List<String>): List<SeedMap> {
        val maps = mutableListOf<SeedMap>()
        var currentMap = SeedMap("N/A", mutableListOf<MapRange>())
        
        input.forEach { line ->
            if (line.endsWith(" map:")) {
                currentMap = SeedMap(line.split(' ')[0], mutableListOf<MapRange>())
                maps.add(currentMap)
            } else if (line.isNotBlank() && !line.startsWith("seeds:")) {
                val destinationRangeStart = line.split(' ')[0].toLong()
                val sourceRangeStart = line.split(' ')[1].toLong()
                val rangeLength = line.split(' ')[2].toLong()
                currentMap.association.add(MapRange(sourceRangeStart, destinationRangeStart, rangeLength))
            }
        }
        
        return maps
    }
    
    fun part1(input: List<String>): Long {
        val seeds = input.first().split(':')[1]
            .split(' ')
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }
        val maps = getMaps(input)
        val locations = seeds.map { getLocation(maps, it) }
        return locations.min()
    }
    
    data class SeedRange(val start: Long, var range: Long)
    
    fun getSeeds(seedLine: String): List<SeedRange> {
        val seedTokens = seedLine.split(' ').filter { it.isNotBlank() }.map { it.trim().toLong() }
        val seeds = seedTokens.chunked(2)
        return seeds.map {
            SeedRange(it[0], it[1])
        }
    }

    fun part2(input: List<String>): Long {
        val seeds = getSeeds(input.first().split(':')[1])
        val maps = getMaps(input)
        var closest = Long.MAX_VALUE
        seeds.forEach {
            LongRange(it.start, it.start + it.range).forEach {
                val loc = getLocation(maps, it)
                if (loc < closest) closest = loc 
            }
        }
        return closest
    }

    val testInput = readInput("Day05_test")
    println("Part 1: ${part1(testInput)}")
    println("Part 2: ${part2(testInput)}")
}
