from matplotlib.pyplot import subplots, cm, tight_layout, savefig, close
from matplotlib.ticker import MaxNLocator
from numpy import linspace
from pandas import read_csv
#!/usr/bin/env python3  # Optional for shebang line
# -*- coding: utf-8 -*-

def visualize_crossroads_simulation(csv_file_path):
    df = read_csv(csv_file_path, sep=";", decimal=".")

    fig, axes = subplots(4, 2, figsize=(20, 15))
    fig.suptitle("Simulation statistic", fontsize=20)
    axes = axes.flatten()

    columns_to_plot = [
        "MAIN_MEAN_QUEUE", "MAIN_MEAN_LOAD",
        "BACKUP_MEAN_QUEUE", "BACKUP_MEAN_LOAD",
        "ERRORS_RATIO", "AVG_TIME_TO_PASS_MESSAGE"
    ]
    column_titles = {
        "MAIN_MEAN_QUEUE": "Mean queue size(main channel)",
        "MAIN_MEAN_LOAD": "Mean load (main channel)",
        "BACKUP_MEAN_QUEUE": "Mean queue size(backup)",
        "BACKUP_MEAN_LOAD": "Mean load(backup)",
        "ERRORS_RATIO": "Errors ratio",
        "AVG_TIME_TO_PASS_MESSAGE": "Avg time for a message to pass the system"
    }

    unique_iterations = df.ITERATION.unique()
    colors = cm.tab10(linspace(0, 1, len(unique_iterations)))

    for i, col in enumerate(columns_to_plot):
        for j, iteration in enumerate(unique_iterations):
            iteration_data = df[df.ITERATION == iteration]
            axes[i].plot(
                iteration_data.TIME, iteration_data[col],
                color=colors[j], alpha=0.7, label=f"Iteration �{int(iteration)}"
            )
        axes[i].set_title(column_titles[col], fontsize=14)
        axes[i].set_xlabel("Simulation time (s)", fontsize=12)
        axes[i].set_ylabel(column_titles[col], fontsize=12)
        axes[i].xaxis.set_major_locator(MaxNLocator(nbins=10))
        axes[i].yaxis.set_major_locator(MaxNLocator(nbins=10))
        axes[i].legend(fontsize=9, loc="upper right")

    tight_layout()

    filename = f"simulation_statistics_time_{int(df.TIME.max())}_iterations_{int(df.ITERATION.max())}.png"
    savefig(filename, dpi=150, bbox_inches="tight")
    close()
    print(f"Saved as  \"{filename}\"...")


if __name__ == "__main__":
    CSV_FILE_PATH = r"../../simulation_data.csv"
    visualize_crossroads_simulation(CSV_FILE_PATH)
