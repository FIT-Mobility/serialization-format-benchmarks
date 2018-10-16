package de.fraunhofer.fit.cscw.mobility.sfb;

import com.beust.jcommander.Parameter;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 15.10.2018
 */
@Getter
class BenchmarksConfig {

    @Parameter(
            names = {"-c", "--comp"},
            description = "Benchmark with no compression (without), compression (with) or both (both)"
    )
    private CompressionEnum compressionTests = CompressionEnum.both;

    @Parameter(
            names = {"-i", "--include"},
            description = "Comma separated regex list to include benchmark in the run. Default value runs all benchmarks"
    )
    private List<String> includeRegex = Collections.singletonList(Benchmarks.class.getSimpleName());

    @Parameter(
            names = {"-e", "--exclude"},
            description = "Comma separated regex list to exclude benchmarks from the run."
    )
    private List<String> excludeRegex = Collections.emptyList();

    public enum CompressionEnum {
        without,
        with,
        both
    }
}
