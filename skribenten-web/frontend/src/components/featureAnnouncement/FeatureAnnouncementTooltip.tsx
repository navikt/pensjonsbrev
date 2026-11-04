import { css, keyframes } from "@emotion/react";
import { SparklesIcon, XMarkIcon } from "@navikt/aksel-icons";
import { BodyLong, BodyShort, Box, Button, Heading, HStack, VStack } from "@navikt/ds-react";
import { useId } from "react";
import { type ArrowRenderProps, type TooltipRenderProps } from "react-joyride";

export const FEATURE_ANNOUNCEMENT_ATTRIBUTE = "data-feature-announcement";
export const FEATURE_ANNOUNCEMENT_LABEL = "Nytt i Skribenten";

export const FEATURE_ANNOUNCEMENT_LOCALE = {
  back: "Forrige",
  close: "Skjønner!",
  last: "Skjønner!",
  next: "Neste",
  skip: "Lukk",
} as const;

export type FeatureAnnouncementStepData = { announcementId: string };

const sideOf = (placement: string) => placement.split("-")[0];

// Transform only: Joyride already fades the tooltip in.
const popIn = keyframes`
  from { transform: translateY(var(--feature-announcement-shift)) scale(0.97); }
  to { transform: none; }
`;

const tooltipMotion = (side: string) => css`
  --feature-announcement-shift: ${side === "top" ? "4px" : "-4px"};

  transform-origin: ${side === "top" ? "bottom" : "top"} left;

  @media (prefers-reduced-motion: no-preference) {
    animation: ${popIn} 220ms cubic-bezier(0.2, 0.9, 0.3, 1.15) both;
  }
`;

/**
 * Arrow with the same colours and border as the part of the tooltip it touches: the coloured header when
 * the tooltip opens below its target. The fill reaches 1px into the tooltip so its border does not cut
 * across the arrow base, making arrow and tooltip read as one shape.
 */
export const FeatureAnnouncementArrow = ({ base, placement, size }: ArrowRenderProps) => {
  const side = sideOf(placement);
  const vertical = side === "top" || side === "bottom";
  // (u, v): u runs along the arrow base, v from the tip (0) towards the tooltip (size).
  const toPoint = (u: number, v: number) => {
    if (side === "top") return `${u},${size - v}`;
    if (side === "left") return `${size - v},${u}`;
    if (side === "right") return `${v},${u}`;
    return `${u},${v}`;
  };

  return (
    <svg
      aria-hidden
      height={vertical ? size : base}
      overflow="visible"
      style={{ display: "block" }}
      width={vertical ? base : size}
    >
      <polygon
        fill={side === "bottom" ? "var(--ax-bg-meta-purple-soft)" : "var(--ax-bg-raised)"}
        points={[toPoint(0, size + 1), toPoint(base / 2, 0), toPoint(base, size + 1)].join(" ")}
      />
      <polyline
        fill="none"
        points={[toPoint(0, size), toPoint(base / 2, 0), toPoint(base, size)].join(" ")}
        stroke="var(--ax-border-meta-purple-subtleA)"
        strokeWidth={1}
      />
    </svg>
  );
};

const ProgressDots = ({ index, size }: { index: number; size: number }) => (
  <HStack align="center" gap="space-6">
    <span className="aksel-sr-only">
      Steg {index + 1} av {size}
    </span>
    {Array.from({ length: size }, (_, dot) => (
      <Box
        aria-hidden
        background={dot === index ? "meta-purple-strong" : "neutral-moderate"}
        borderRadius="full"
        height="0.375rem"
        key={dot}
        width={dot === index ? "1rem" : "0.375rem"}
      />
    ))}
  </HStack>
);

export const FeatureAnnouncementTooltip = ({
  backProps,
  index,
  isLastStep,
  primaryProps,
  size,
  skipProps,
  step,
  tooltipProps,
}: TooltipRenderProps) => {
  const titleId = useId();
  const contentId = useId();
  const { announcementId } = step.data as FeatureAnnouncementStepData;
  const isWalkthrough = size > 1;

  return (
    // tooltipProps make this a modal alert dialog; Joyride traps focus in it and restores focus on close.
    <Box
      {...tooltipProps}
      aria-describedby={contentId}
      aria-labelledby={titleId}
      background="raised"
      borderColor="meta-purple-subtleA"
      borderRadius="12"
      borderWidth="1"
      css={tooltipMotion(sideOf(step.placement))}
      maxWidth="calc(100vw - 2rem)"
      overflow="hidden"
      shadow="dialog"
      width="22.5rem"
      {...{ [FEATURE_ANNOUNCEMENT_ATTRIBUTE]: announcementId }}
    >
      <Box
        background="meta-purple-soft"
        borderColor="meta-purple-subtleA"
        borderWidth="0 0 1 0"
        paddingBlock="space-8"
        paddingInline="space-16"
      >
        <HStack align="center" gap="space-8" justify="space-between" minHeight="2rem" wrap={false}>
          <HStack align="center" gap="space-8" wrap={false}>
            <span css={{ display: "inline-flex", color: "var(--ax-text-meta-purple)" }}>
              <SparklesIcon aria-hidden fontSize="1.5rem" />
            </span>
            <BodyShort css={{ color: "var(--ax-text-meta-purple)" }} weight="semibold">
              {FEATURE_ANNOUNCEMENT_LABEL}
            </BodyShort>
          </HStack>
          {isWalkthrough && (
            <Button
              {...skipProps}
              icon={<XMarkIcon aria-hidden />}
              size="small"
              title={undefined}
              type="button"
              variant="tertiary-neutral"
            />
          )}
        </HStack>
      </Box>
      <VStack gap="space-16" padding="space-16">
        <VStack gap="space-8">
          <Heading id={titleId} level="2" size="xsmall">
            {step.title}
          </Heading>
          <BodyLong as="div" id={contentId} size="small">
            {step.content}
          </BodyLong>
        </VStack>
        <HStack align="center" gap="space-8" justify={isWalkthrough ? "space-between" : "end"}>
          {isWalkthrough && <ProgressDots index={index} size={size} />}
          <HStack gap="space-8">
            {index > 0 && (
              <Button {...backProps} size="small" title={undefined} type="button" variant="secondary">
                {FEATURE_ANNOUNCEMENT_LOCALE.back}
              </Button>
            )}
            <Button {...primaryProps} size="small" title={undefined} type="button" variant="primary">
              {isLastStep ? FEATURE_ANNOUNCEMENT_LOCALE.last : FEATURE_ANNOUNCEMENT_LOCALE.next}
            </Button>
          </HStack>
        </HStack>
      </VStack>
    </Box>
  );
};
